package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class MineracaoService {

    private static String geraF(int numF) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numF; i++) {
            sb.append("f");
        }
        return sb.toString();
    }

    public BigInteger geraDificuldade() {
        return new BigInteger(geraF(60), 16);
    }

}
