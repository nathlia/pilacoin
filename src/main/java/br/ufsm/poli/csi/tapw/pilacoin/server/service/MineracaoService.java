package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilaCoinRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;

@Service
public class MineracaoService {

    @Autowired
    private MineracaoController mineracaoController = new MineracaoController();
    @Autowired
    private PilaCoinRepository pilaCoinRepository;
    @Autowired
    private PilaCoinService pilaCoinService;

    @Autowired
    private RegistraUsuarioService registraUsuarioService;


    @SneakyThrows
    public void initPilacoint(boolean minerar) {
        PublicKey publicKey = registraUsuarioService.getPublicKey();

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("                            START: MINERAÇÃO ");
        System.out.println("--------------------------------------------------------------------------------------------");

        while (minerar) {
            SecureRandom rnd = new SecureRandom();

            PilaCoin pilaCoin = new PilaCoin();
            pilaCoin.setDataCriacao(new Date());
            pilaCoin.setChaveCriador(publicKey.getEncoded());
            pilaCoin.setNonce(new BigInteger(128, rnd).abs());
            pilaCoin.setIdCriador("Nathalia");
            pilaCoin.setStatus(PilaCoin.AG_VALIDACAO);

            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.digest(pilaJson.getBytes("UTF-8"));
            byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));

            Minerador(hash, pilaCoin);
        }

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("                            FINISH: MINERAÇÃO ");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    @SneakyThrows
    private void Minerador(byte[] hash, PilaCoin pilaCoin) {
        BigInteger nonce = pilaCoin.getNonce();
        BigInteger dificuldade = mineracaoController.getDificuldade();
        BigInteger numHash = new BigInteger(hash).abs();
        SecureRandom rnd = new SecureRandom();

        do {
            //----------------------------------------
            nonce = new BigInteger(128, rnd).abs();
            pilaCoin.setNonce(nonce);
            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.digest(pilaJson.getBytes("UTF-8"));
            byte[] newHash = md.digest(pilaJson.getBytes("UTF-8"));
            numHash = new BigInteger(newHash).abs();
            //----------------------------------------
        } while (dificuldade.compareTo(numHash) < 0);

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println(" 🔘 Pilacoin: ");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println(" ✔ = " + numHash);
        System.out.println(" 💥 = " + dificuldade);
        System.out.println(" nonce: " + pilaCoin.getNonce());
        System.out.println(" compare: " + dificuldade.compareTo(numHash));
        System.out.println("--------------------------------------------------------------------------------------------");

        PilaCoin pilaSaved = pilaCoinRepository.save(pilaCoin);

        if (pilaSaved.getId() != null) {
            System.out.println("Pila saved: " + pilaSaved.getId());
        } else {
            System.out.println("ERROR Creating pila");
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        pilaCoin.setIdCriador(null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String pilaJson = objectMapper.writeValueAsString(pilaCoin);
        System.out.println(pilaJson);
        System.out.println("--------------------------------------------------------------------------------------------");
        pilaCoinService.getPilacoinAndSend(pilaJson);
    }

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @ToString
//    private static class pilaToJson {
//        private String assinaturaMaster;
//        private String chaveCriador;
//        private String dataCriacao;
//        private Long id;
//        private String nonce;
//        private String status;
//    }


}



