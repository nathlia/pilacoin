package br.ufsm.poli.csi.tapw.pilacoin.util;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;


public class Mineracao {

    //private static final BigInteger DIFICULDADE = BigInteger.valueOf(10000000);
    private static final BigInteger DIFICULDADE = new BigInteger(geraF(60), 16);
    //GeraChaves geraChaves = new GeraChaves();



    public Mineracao() throws NoSuchAlgorithmException {
    }

    private static String geraF(int numF) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numF; i++) {
            sb.append("f");
        }
        return sb.toString();
    }

    @SneakyThrows
    public static void main(String[] args) {

        String path = "C:\\Users\\aluno\\Downloads\\pilacoin-rest_nathalia";

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        SecureRandom rnd = new SecureRandom();

        KeyPair kp = kpg.generateKeyPair();
        //KeyPair kp = new GeraChaves().LoadKeyPair(path, String.valueOf(keyGen));
        PilaCoin pilaCoin = PilaCoin.builder()
                .dataCriacao(new Date())
                .chaveCriador(kp.getPublic().getEncoded())
                .nonce(new BigInteger(128, rnd).abs())
                .idCriador("Nathalia")
                .build();

        String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
        System.out.println(pilaJson);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.digest(pilaJson.getBytes("UTF-8"));
        byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));

//        comparar hash, transforma em numero
//        BigInteger numHash = new BigInteger(hash).abs();
//        if (numHash.compareTo(BigInteger.valueOf(100000000)) < 0) {
//         System.out.println(" ✨ Minerou!");
//        } else {
//            System.out.println(" 💥 Nao mineirou...");
//        }

        Minerador(hash, pilaCoin);
    }

    private static void Minerador (byte[] hash, PilaCoin pilaCoin) {
        BigInteger numHash = new BigInteger(hash).abs();
        int cont = 0;
        int tentativas = 10000000;
        while (tentativas > 0) {
            //coin
            if (numHash.compareTo(DIFICULDADE) < 0) {
                cont++;
            }
            numHash = new BigInteger(hash).abs();
            tentativas--;
        }

        if (cont == 0) {
            System.out.println(" 💥 Nao mineirou...");
        } else {
            System.out.println(" ✨ Minerou! " + cont + " vezes");
        }
    }



//    private KeyPair getChaves() {
//        JFileChooser jFileChooser = new JFileChooser("Downloads");
//        System.out.println("Selecionando o arquivo...");
//
//        if (jFileChooser.showDialog(new JFrame(), "OK") == JFileChooser.APPROVE_OPTION) {
//            // descritor do arquivo
//            File file = jFileChooser.getSelectedFile();
//
//            //KeyPair kp = readFromInputStream(file);
//
//        }
//        return kp;
//
//    }


}


// salvar par de chaves em um arquivo
// gera par, get encode, salva em um arquivo
// ler par de chaves
// magic number
