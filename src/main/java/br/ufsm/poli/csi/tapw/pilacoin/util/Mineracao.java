package br.ufsm.poli.csi.tapw.pilacoin.util;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;

public class Mineracao implements Runnable {

    //private static final BigInteger DIFICULDADE = BigInteger.valueOf(10000000);
    //private static final BigInteger DIFICULDADE = new BigInteger(geraF(60), 16);
    //GeraChaves geraChaves = new GeraChaves();
    @Autowired
    private static WebSocketClient webSocketClient = new WebSocketClient();
//
//    public Mineracao() throws NoSuchAlgorithmException {
//    }
//
//    private static String geraF(int numF) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < numF; i++) {
//            sb.append("f");
//        }
//        return sb.toString();
//    }

    @Override
    public void run() {
        new Thread(new InitPilaCoin()).start();
//       new InitPilaCoin();
    }

    private class InitPilaCoin implements Runnable {

        @Override
        public void run() {
            try {
                String path = "C:\\Users\\aluno\\Downloads\\pilacoin-rest_nathalia";

                RegistraUsuarioService registraUsuarioService = new RegistraUsuarioService();
                PublicKey publicKey = registraUsuarioService.getPrivateKey();

                SecureRandom rnd = new SecureRandom();

                PilaCoin pilaCoin = PilaCoin.builder()
                        .dataCriacao(new Date())
                        .chaveCriador(publicKey.getEncoded())
                        .nonce(new BigInteger(128, rnd).abs())
                        .idCriador("Nathalia")
                        .build();

                String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
//                System.out.println(pilaJson);
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.digest(pilaJson.getBytes("UTF-8"));
                byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));

                Minerador(hash, pilaCoin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @SneakyThrows
    private static void Minerador (byte[] hash, PilaCoin pilaCoin) {
        //1329227995784915872903807060280344575
        BigInteger nonce = pilaCoin.getNonce();

        BigInteger dificuldade = webSocketClient.getDificuldade();
        System.out.println(dificuldade);
        BigInteger numHash = new BigInteger(hash).abs();
        BigInteger dificuldadeStatic = new BigInteger("1766847064778384329583297500742918515827483896875618958121606201292619775").abs();
        SecureRandom rnd = new SecureRandom();

        // gerar nonce
        // setar pilacoin
        // converte string
        // gera hash pilacoin
        // comparar hash com a dificuldade = minerado

        int cont = 0;
        int tentativas = 0;
        while (numHash.compareTo(dificuldadeStatic) > 0) {
            //coin
            if (numHash.compareTo(dificuldadeStatic) < 0) {
                System.out.println(" 🔘 Pilacoin: " + pilaCoin);
                cont++;
                break;
            }
            //----------------------------------------
            nonce = new BigInteger(128, rnd).abs();
            pilaCoin.setNonce(nonce);
            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
            //System.out.println(pilaJson);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.digest(pilaJson.getBytes("UTF-8"));
            byte[] newHash = md.digest(pilaJson.getBytes("UTF-8"));
            numHash = new BigInteger(newHash).abs();

            //----------------------------------------
            tentativas++;
//            System.out.println("loop:" + tentativas);
        }

        if (cont == 0) {
            System.out.println(" 💥 Nao mineirou...");
        } else {
            System.out.println(" ✨ Minerou! " + cont + " vezes");
        }
    }

    @SneakyThrows
    private byte[] geraPilacoin() {
        RegistraUsuarioService registraUsuarioService = new RegistraUsuarioService();
        PublicKey publicKey = registraUsuarioService.getPrivateKey();

        SecureRandom rnd = new SecureRandom();

        //KeyPair kp = kpg.generateKeyPair();
        //KeyPair kp = new GeraChaves().LoadKeyPair(path, String.valueOf(keyGen));
        PilaCoin pilaCoin = PilaCoin.builder()
                .dataCriacao(new Date())
                .chaveCriador(publicKey.getEncoded())
                .nonce(new BigInteger(128, rnd).abs())
                .idCriador("Nathalia")
                .build();

        String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
        System.out.println(pilaJson);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.digest(pilaJson.getBytes("UTF-8"));
        byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));
        return hash;
    }
}


// salvar par de chaves em um arquivo
// gera par, get encode, salva em um arquivo
// ler par de chaves
// nonce



