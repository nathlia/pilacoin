package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.model.Bloco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;

@Service
public class ValidarBlocoService {

    @Autowired
    private RegistraUsuarioService registraUsuarioService;

    public void descobrirBloco(Bloco bloco) {
        PublicKey publicKey = registraUsuarioService.getPublicKey();


        System.out.println(" ============================================================================================");
        System.out.println("||                                  START: BLOCO                                        ||");
        System.out.println(" ============================================================================================");

        while (true) {
            SecureRandom srd = new SecureRandom();
            String blocoJson;




            System.out.println(" ============================================================================================");
            System.out.println("||                                  FINISH: BLOCO                                       ||");
            System.out.println(" ============================================================================================");

        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BlocoIsMine {
        private Date dataCriacao;
        private byte[] chaveCriador;
        private BigInteger nonce;
    }
}
