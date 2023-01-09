package br.ufsm.poli.csi.tapw.pilacoin.util;//package br.ufsm.poli.csi.tapw.pilacoin.util;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class GeraChaves {

    @SneakyThrows
    public static void main(String[] args) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        SecureRandom rnd = new SecureRandom();

        KeyPair kp = kpg.generateKeyPair();


        try (PrintWriter out = new PrintWriter("private.key")) {
            out.println(kp.getPrivate());
        }

        try (PrintWriter out = new PrintWriter("public.key")) {
            out.println(kp.getPublic());
        }
    }

    public KeyPair LoadKeyPair(String path, String algorithm)
            throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        // Read Public Key.
        File filePublicKey = new File(path + "/public.key");
        FileInputStream fis = new FileInputStream(path + "/public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(path + "/private.key");
        fis = new FileInputStream(path + "/private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//        kpg.generateKeyPair(encodedPrivateKey, e);


        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PKCS8EncodedKeySpec  publicKeySpec = new PKCS8EncodedKeySpec (
                encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        X509EncodedKeySpec  privateKeySpec = new X509EncodedKeySpec (
                encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
}

//package com.java2s;

//import java.io.DataOutputStream;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//
//import java.security.PrivateKey;
//import java.security.PublicKey;
//
//public class GeraChaves {
//    public static void main(String[] args) {
//        genRSAKeyPairAndSaveToFile();
//    }
//
//    public static void genRSAKeyPairAndSaveToFile() {
//        genRSAKeyPairAndSaveToFile(2048, "");
//    }/*from  ww w  .j  a v  a  2 s  . co  m*/
//
//    public static void genRSAKeyPairAndSaveToFile(String dir) {
//        genRSAKeyPairAndSaveToFile(2048, dir);
//    }
//
//    public static void genRSAKeyPairAndSaveToFile(int keyLength, String dir) {
//        KeyPair keyPair = genRSAKeyPair(keyLength);
//
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//
//        DataOutputStream dos = null;
//        try {
//            dos = new DataOutputStream(new FileOutputStream(dir
//                    + "rsaPublicKey"));
//            dos.write(publicKey.getEncoded());
//            dos.flush();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (dos != null) {
//                try {
//                    dos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//        try {
//            dos = new DataOutputStream(new FileOutputStream(dir
//                    + "rsaPrivateKey"));
//            dos.write(privateKey.getEncoded());
//            dos.flush();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (dos != null)
//                try {
//                    dos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//    }
//
//    public static KeyPair genRSAKeyPair() {
//        return genRSAKeyPair(2048);
//    }
//
//    public static KeyPair genRSAKeyPair(int keyLength) {
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator
//                    .getInstance("RSA");
//            keyPairGenerator.initialize(keyLength);
//            return keyPairGenerator.generateKeyPair();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
