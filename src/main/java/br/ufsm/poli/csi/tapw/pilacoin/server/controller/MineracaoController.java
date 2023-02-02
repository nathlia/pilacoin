package br.ufsm.poli.csi.tapw.pilacoin.server.controller;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
import br.ufsm.poli.csi.tapw.pilacoin.server.model.Bloco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;

@Controller
public class MineracaoController {

    @Autowired
    private WebSocketClient webSocketClient;

    @MessageMapping("/dificuldade")
    public BigInteger getDificuldade() {
        return webSocketClient.getDificuldade();
    }

    @MessageMapping("/validaMineracao")
    public PilaCoin getPilaToValidate() {
        return webSocketClient.getPilaCoin();
    }

    @MessageMapping("/descobrirNovoBloco")
    public Bloco getNovoBloco() {
        return webSocketClient.getBloco();
    }

}
