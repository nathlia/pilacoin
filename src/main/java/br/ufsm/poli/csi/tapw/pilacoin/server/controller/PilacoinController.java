package br.ufsm.poli.csi.tapw.pilacoin.server.controller;


import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
import br.ufsm.poli.csi.tapw.pilacoin.server.jobr.ProcessScheduler;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilaCoinRepository;
import br.ufsm.poli.csi.tapw.pilacoin.server.service.PilaCoinService;
import br.ufsm.poli.csi.tapw.pilacoin.server.service.ValidaPilaService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PilacoinController {
    @Autowired
    ProcessScheduler processScheduler;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private PilaCoinService pilaCoinService;

    @Autowired
    private ValidaPilaService validaPilaService;

    final
    PilaCoinRepository pilacoinRepository;

    public PilacoinController(PilaCoinRepository pilacoinRepository) {
        this.pilacoinRepository = pilacoinRepository;
    }

    @GetMapping("/pilacoins")
    public ResponseEntity<List<PilaCoin>> getAllPilacoins(@RequestParam(required = false) String name) {
        try {
            List<PilaCoin> pilacoins = new ArrayList<>();

            if (name == null) {
                pilacoins.addAll(pilacoinRepository.findAll());
            }

            if (pilacoins.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(pilacoins, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pilacoins/{id}")
    public ResponseEntity<PilaCoin> getPilacoinById(@PathVariable("id") Long id) {
        Optional<PilaCoin> pilacoinData = pilacoinRepository.findById(id);

        return pilacoinData.map(pilacoin -> new ResponseEntity<>(pilacoin, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    private boolean isStopped = false;

    public boolean getIsStopped() {
        return isStopped;
    }

    @GetMapping("/minerar/{minerar}")
    public ResponseEntity<String> scheduleRecurrently(@PathVariable boolean minerar) {
        if (minerar == true) {
            System.out.println("~ INICIANDO MINERAÇÃO JOB ~");
            isStopped = false;
            processScheduler.processOnce(minerar);
            return ResponseEntity.ok().body("~ INICIANDO MINERAÇÃO JOB ~");
        } else {
            System.out.println("~ PARANDO MINERAÇÃO JOB ~");
            isStopped = true;
            return ResponseEntity.ok().body("~ PARANDO MINERAÇÃO JOB ~");
        }
    }

    @PostMapping("/pilacoin/")
    public ResponseEntity<String> sendCoinForValidadion(@RequestBody PilaCoin pilacoin) {
        BigInteger dificuldade = webSocketClient.getDificuldade();
        if (isValidPilacoin(pilacoin, dificuldade)) {
            // send the pilacoin to the server
            pilaCoinService.postPilacoin(pilacoin);
            return ResponseEntity.ok("Pilacoin válido e enviado com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Pilacoin inválido");
        }
    }

    @GetMapping("pilacoin/validaPilaOutroUsuario")
    public ResponseEntity<String> validarCoin() {
        BigInteger dificuldade = webSocketClient.getDificuldade();
        PilaCoin pilaCoin = webSocketClient.getPilaCoin();
        if (isValidPilacoin(pilaCoin, dificuldade)) {
            // send the pilacoin to the server
            return ResponseEntity.ok("Pilacoin válido !");
        } else {
            return ResponseEntity.badRequest().body("Pilacoin inválido");
        }
    }

    @SneakyThrows
    private boolean isValidPilacoin(PilaCoin pilacoin, BigInteger dificuldade) {
        if (validaPilaService.validarPila(pilacoin) == true) {
            return true;
        };
        return false;
    }
}
