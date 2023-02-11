package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.PilacoinController;
import br.ufsm.poli.csi.tapw.pilacoin.server.model.PilacoinsOutroUsuario;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilacoinsOutroUsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ValidaPilaService {

    @Autowired
    private MineracaoController mineracaoController = new MineracaoController();

    @Autowired
    private PilacoinsOutroUsuarioRepository pilacoinsOutroUsuarioRepository;

    @Autowired
    private PilaCoinService pilaCoinService;

    @Autowired
    private RegistraUsuarioService registraUsuarioService;


    public boolean validarPila(PilaCoin pilaCoinDoColega) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        BigInteger dificuldade = mineracaoController.getDificuldade();
        System.out.println(" ============================================================================================");
        System.out.println("||                                  START: VALIDAR PILA                                    ||");


        if (dificuldade != null) {
            PilaCoin pilaCoin = new PilaCoin();
            pilaCoin.setDataCriacao(pilaCoinDoColega.getDataCriacao());
            pilaCoin.setChaveCriador(pilaCoinDoColega.getChaveCriador());
            pilaCoin.setNonce(pilaCoinDoColega.getNonce());


            if (pilaCoinService.isNonceValid(pilaCoin, dificuldade)) {
                System.out.println("||                                 ✔ PILACOIN VALIDO                                      ||");
                System.out.println("||                           " + pilaCoin.getNonce() + "                        ||");

                pilaCoin.setStatus(PilaCoin.VALIDO);
                byte[] hashValido = pilaCoinService.getHash(pilaCoin);
                enviaPilaValidado(pilaCoinDoColega, hashValido);
                return true;

            } else {
                pilaCoin.setStatus(PilaCoin.INVALIDO);
                byte[] hashValido = pilaCoinService.getHash(pilaCoin);
                enviaPilaValidado(pilaCoinDoColega, hashValido);
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

    @SneakyThrows
    public void salvaPilaDoColega() {

       ArrayList<PilaCoin> listPilasDoColega =  mineracaoController.getListPilacoins();

       int i;
       for (i = 0; i < listPilasDoColega.size(); i++) {
           PilacoinsOutroUsuario pilacoinsOutroUsuario = new PilacoinsOutroUsuario();
           pilacoinsOutroUsuario.setId(listPilasDoColega.get(i).getId());
           pilacoinsOutroUsuario.setAssinaturaMaster(listPilasDoColega.get(i).getAssinaturaMaster());
           pilacoinsOutroUsuario.setNonce(listPilasDoColega.get(i).getNonce());
           pilacoinsOutroUsuario.setIdCriador(listPilasDoColega.get(i).getIdCriador());
           pilacoinsOutroUsuario.setChaveCriador(listPilasDoColega.get(i).getChaveCriador());

           boolean isValid = validarPila(listPilasDoColega.get(i));

           if (isValid == true) {
               pilacoinsOutroUsuario.setStatus(PilacoinsOutroUsuario.VALIDO);
           } else {
               pilacoinsOutroUsuario.setStatus(PilacoinsOutroUsuario.INVALIDO);
           }

           PilacoinsOutroUsuario pilaSaved = pilacoinsOutroUsuarioRepository.save(pilacoinsOutroUsuario);
           if (pilaSaved.getId() != null) {
               System.out.println(" 🤝 Pila do colega saved: " + pilaSaved.getNonce());
           } else {
               System.out.println(" ❌ ERROR Saving pila do colega");
           }
       }
        System.out.println("--------------------------------------------------------------------------------------------");
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
