package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PilaCoinService {
    @Value("${endereco.server}")
    private String enderecoServer;

//    Minerar minerar = new Minerar();

    @PostConstruct
    public void init() {
       // System.out.println("Pilacoin enviado: " + enviaPila());
    }

    public void enviaPila(PilaCoin pilaCoin) {

    }

}
