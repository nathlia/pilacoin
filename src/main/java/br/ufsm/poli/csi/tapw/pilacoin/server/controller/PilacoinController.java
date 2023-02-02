package br.ufsm.poli.csi.tapw.pilacoin.server.controller;


import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
import br.ufsm.poli.csi.tapw.pilacoin.server.jobr.ProcessScheduler;
import br.ufsm.poli.csi.tapw.pilacoin.server.service.PilaCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@CrossOrigin(origins = "*")
public class PilacoinController {
    @Autowired
    ProcessScheduler processScheduler;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private PilaCoinService pilaCoinService;


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

    @PostMapping("pilacoin/validaPilaOutroUsuario")
    public ResponseEntity<String> validarCoin(@RequestBody PilaCoin pilacoin) {
        BigInteger dificuldade = webSocketClient.getDificuldade();
        if (isValidPilacoin(pilacoin, dificuldade)) {
            // send the pilacoin to the server
            return ResponseEntity.ok("Pilacoin válido e enviado com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Pilacoin inválido");
        }
    }

    private boolean isValidPilacoin(PilaCoin pilacoin, BigInteger dificuldade) {
        // validate the pilacoin using the difficulty
        return true;
    }
}
