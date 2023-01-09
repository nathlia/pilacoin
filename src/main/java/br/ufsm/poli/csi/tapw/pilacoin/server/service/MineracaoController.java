package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class MineracaoController {
    private Minerar minerar;
    private WebSocketClient webSocketClient;

    public BigInteger getDificuldade() {
        return webSocketClient.getDificuldade();
    }
}
