package br.ufsm.poli.csi.tapw.pilacoin.server.controller;

import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Controller
public class MineracaoController {

    @Autowired
    private WebSocketClient webSocketClient;

    @MessageMapping("/dificuldade")
    public BigInteger getDificuldade() {
        return webSocketClient.getDificuldade();
    }

}
