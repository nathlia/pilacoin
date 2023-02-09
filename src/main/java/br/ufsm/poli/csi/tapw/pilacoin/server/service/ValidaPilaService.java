package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.PilacoinController;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Service
public class ValidaPilaService {

    @Autowired
    private MineracaoController mineracaoController = new MineracaoController();

    @Autowired
    private PilacoinController pilacoinController;

    @Autowired
    private PilaCoinService pilaCoinService;

    @Autowired
    private RegistraUsuarioService registraUsuarioService;


    public boolean validarPila(PilaCoin pilaCoinDoColega) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        BigInteger dificuldade = mineracaoController.getDificuldade();
        System.out.println(" ============================================================================================");
        System.out.println("||                                  START: VALIDAR PILA                                     ||");


        if (dificuldade != null) {
            PilaCoin pilaCoin = new PilaCoin();
            pilaCoin.setDataCriacao(pilaCoinDoColega.getDataCriacao());
            pilaCoin.setChaveCriador(pilaCoinDoColega.getChaveCriador());
            pilaCoin.setNonce(pilaCoinDoColega.getNonce());


            if (pilaCoinService.isNonceValid(pilaCoin, dificuldade)) {
                System.out.println("||                                 ✔ PILACOIN VALIDO                                       ||");
                System.out.println("||                           " + pilaCoin.getNonce() + "                        ||");

                byte[] hashValido = pilaCoinService.getHash(pilaCoin);
                enviaPilaValidado(pilaCoinDoColega, hashValido);
                return true;

            } else {
                System.out.println("||                                 ❌ PILACOIN INVALIDO                                     ||");
                System.out.println("||                           " + pilaCoin.getNonce() + "                        ||");
            }

            System.out.println("||                                  FINISH: VALIDAR PILA                                    ||");
            System.out.println(" ============================================================================================");

        }
        return false;
    }
    @SneakyThrows
    public void enviaPilaValidado(PilaCoin pilaDoColegaValido, byte[] hashColega) {

        PublicKey publicKey = registraUsuarioService.getPublicKey();
        PrivateKey privateKey = registraUsuarioService.getPrivateKey();

        ValidPilaCoin validPilaCoin = new ValidPilaCoin();
        validPilaCoin.setChavePublica(publicKey.getEncoded());
        validPilaCoin.setNonce(pilaDoColegaValido.getNonce());
        validPilaCoin.setTipo("PILA");
        validPilaCoin.setHashPilaBloco(Base64.encodeBase64String(hashColega));

        byte[] hashValidPila = pilaCoinService.getHash(pilaDoColegaValido);

        Cipher cipherRSA = Cipher.getInstance("RSA");
        cipherRSA.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] hashCriptografada = cipherRSA.doFinal(hashValidPila);
        validPilaCoin.setAssinatura(Base64.encodeBase64String(hashCriptografada));


        pilaCoinService.postValidPilacoin(validPilaCoin);
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValidPilaCoin {
        private Date dataCriacao;
        private byte[] chavePublica;
        private BigInteger nonce;
        private String tipo;
        private String hashPilaBloco;
        private String assinatura;
    }

}
