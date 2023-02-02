package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilaCoinRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Date;

@Service
public class MineracaoService {

    @Autowired
    private MineracaoController mineracaoController = new MineracaoController();

    @Autowired
    private PilaCoinService pilaCoinService;

    @Autowired
    private RegistraUsuarioService registraUsuarioService;


    @SneakyThrows
    public void initPilacoint(boolean minerar) {
        PublicKey publicKey = registraUsuarioService.getPublicKey();

        System.out.println(" ============================================================================================");
        System.out.println("||                                  START: MINERAÇÃO                                        ||");
        System.out.println(" ============================================================================================");

        while (minerar) {
            PilaCoin pilaCoin = new PilaCoin();
            pilaCoin.setDataCriacao(new Date());
            pilaCoin.setChaveCriador(publicKey.getEncoded());

            Minerador(pilaCoin);
        }

        System.out.println(" ============================================================================================");
        System.out.println("||                                  FINISH: MINERAÇÃO                                       ||");
        System.out.println(" ============================================================================================");
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
                SecureRandom rnd = new SecureRandom();
            do {
                nonce = new BigInteger(128, rnd).abs();
                pilaCoin.setNonce(nonce);
                String pilaJson = objectMapper.writeValueAsString(pilaCoin);
                byte[] newHash = md.digest(pilaJson.getBytes(StandardCharsets.UTF_8));
                numHash = new BigInteger(newHash).abs();
                //----------------------------------------
            } while (dificuldade.compareTo(numHash) < 0);

            int compare = dificuldade.compareTo(numHash) ;
            String compareResult;

            if (compare > 0) {
                compareResult = "Dificuldade eh maior que hash.";
            } else if (compare < 0) {
                compareResult = "Dificuldade eh menor que hash.";
            } else {
                compareResult = "Dificuldade eh igual hash.";
            }

            System.out.println("============================================================================================");
            System.out.println(" 🔘 Pilacoin: ");
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println(" ✔ = " + numHash);
            System.out.println(" 💥 = " + dificuldade);
            System.out.println(" nonce: " + pilaCoin.getNonce());
            System.out.println(" compare: "+ compare + ". " + compareResult);
            System.out.println("============================================================================================");
            pilaCoinService.getPilacoinAndSend(pilaCoin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



