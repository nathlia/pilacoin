package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.PilacoinController;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Date;

@Service
public class MineracaoService {

    @Autowired
    private MineracaoController mineracaoController = new MineracaoController();

    @Autowired
    private PilacoinController pilacoinController ;

    @Autowired
    private PilaCoinService pilaCoinService;

    @Autowired
    private RegistraUsuarioService registraUsuarioService;

    @Autowired
    private ValidaPilaService validaPilaService;

    @SneakyThrows
    public void initPilacoint(boolean minerar) {
        PublicKey publicKey = registraUsuarioService.getPublicKey();

        System.out.println(" ============================================================================================");
        System.out.println("||                                  START: MINERAÇÃO                                        ||");
        System.out.println(" ============================================================================================");

        while (minerar == true) {
            PilaCoin pilaCoin = new PilaCoin();
            pilaCoin.setDataCriacao(new Date());
            pilaCoin.setChaveCriador(publicKey.getEncoded());

            Minerador(pilaCoin);

            if (pilacoinController.getIsStopped() == true) {
                System.out.println(" ============================================================================================");
                System.out.println("||                                  FINISH: MINERAÇÃO                                       ||");
                System.out.println(" ============================================================================================");
                break;
            }
        }
    }

    private void Minerador( PilaCoin pilaCoin) {
        try {
            //PublicKey publicKey = registraUsuarioService.getPublicKey();
            BigInteger nonce = BigInteger.ZERO;
            BigInteger numHash = BigInteger.ZERO;
            BigInteger dificuldade = mineracaoController.getDificuldade();

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            do {
                SecureRandom rnd = new SecureRandom();
                nonce = new BigInteger(128, rnd).abs();
                pilaCoin.setNonce(nonce);
                String pilaJson = objectMapper.writeValueAsString(pilaCoin);
                byte[] newHash = md.digest(pilaJson.getBytes(StandardCharsets.UTF_8));
                numHash = new BigInteger(newHash).abs();
                //----------------------------------------
            } while (numHash.compareTo(dificuldade) >= 0);

            int compare = numHash.compareTo(dificuldade) ;
            String compareResult;

            if (compare > 0) {
                compareResult = "hash eh maior que dificuldade.";
            } else if (compare < 0) {
                compareResult = "hash eh menor que dificuldade.";
            } else {
                compareResult = "hash eh igual dificuldade.";
            }

            System.out.println("============================================================================================");
            System.out.println(" 🔘 Pilacoin: ");
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println(" ✔ = " + numHash);
            System.out.println(" 💥 = " + dificuldade);
            System.out.println(" nonce: " + pilaCoin.getNonce());
            System.out.println(" compare: "+ compare + ". " + compareResult);
            System.out.println("============================================================================================");

            if (pilaCoinService.isNonceValid(pilaCoin, dificuldade)) {
                System.out.println(" 🤍  PILACOIN IS VALID!  🤍  PILACOIN IS VALID!  🤍  PILACOIN IS VALID!  🤍  PILACOIN IS V");
                System.out.println("============================================================================================");
                pilaCoinService.savePila(pilaCoin);
                pilaCoinService.getPilacoinAndSend(pilaCoin);
            }

            if (validaPilaService.validarPila(pilaCoin)) {
                System.out.println(" ❤  PILACOIN IS VALID!  ❤  PILACOIN IS VALID!  ❤  PILACOIN IS VALID!  ❤  PILACOIN IS V");
                System.out.println("============================================================================================");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



