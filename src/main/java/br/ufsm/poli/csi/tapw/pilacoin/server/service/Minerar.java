//package br.ufsm.poli.csi.tapw.pilacoin.server.service;
//
//import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
//import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
//import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
//import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilaCoinRepository;
//import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.UsuarioRepository;
//import br.ufsm.poli.csi.tapw.pilacoin.server.service.Mineracao;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.jobrunr.jobs.mappers.JobMapper;
//import org.jobrunr.scheduling.JobScheduler;
//import org.jobrunr.storage.InMemoryStorageProvider;
//import org.jobrunr.storage.StorageProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.event.EventListener;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.messaging.SessionConnectEvent;
//
//import javax.validation.constraints.Min;
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//import java.security.*;
//import java.util.Date;
//
//@Service
//public class Minerar {
//
//    private Boolean parar = true;
//    private Thread mineracao;
//
//    public void iniciaMineracao() {
//        synchronized (Minerar.class) {
//            parar = false;
//            if (mineracao == null) {
//                //mineracao = new Thread(new ThreadMineracao());
//                mineracao.start();
//            } else {
//                Minerar.this.notify();
//            }
//
//        }
//    }
//
//    public void pararMineracao() {
//        synchronized (Minerar.this) {
//            parar = true;
//        }
//    }
//
//    @Override
//    public void run() {
//        new Thread(new Mineracao.InitPilaCoin()).start();
////       new InitPilaCoin();
//    }
//
//    private class InitPilaCoin {
//        @Override
//        public void run() {
//            try {
//                String path = "C:\\Users\\aluno\\Downloads\\pilacoin-rest_nathalia";
//
//                RegistraUsuarioService registraUsuarioService = new RegistraUsuarioService();
//                PublicKey publicKey = registraUsuarioService.getPrivateKey();
//
//                SecureRandom rnd = new SecureRandom();
//
//                PilaCoin pilaCoin = PilaCoin.builder()
//                        .dataCriacao(new Date())
//                        .chaveCriador(publicKey.getEncoded())
//                        .nonce(new BigInteger(128, rnd).abs())
//                        .idCriador("Nathalia")
//                        .build();
//
//                String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
////                System.out.println(pilaJson);
//                MessageDigest md = MessageDigest.getInstance("SHA-256");
//                md.digest(pilaJson.getBytes("UTF-8"));
//                byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));
//
//                Minerador(hash, pilaCoin);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @SneakyThrows
//    private void Minerador(byte[] hash, PilaCoin pilaCoin) {
//        //1329227995784915872903807060280344575
//        BigInteger nonce = pilaCoin.getNonce();
//
////        BigInteger dificuldade = WebSocketClient.getDificuldade();
//        ///System.out.println(dificuldade);
//        BigInteger numHash = new BigInteger(hash).abs();
//        BigInteger dificuldadeStatic = new BigInteger("1766847064778384329583297500742918515827483896875618958121606201292619775").abs();
//        SecureRandom rnd = new SecureRandom();
//
//        // gerar nonce
//        // setar pilacoin
//        // converte string
//        // gera hash pilacoin
//        // comparar hash com a dificuldade = minerado
//
//        int cont = 0;
//        do {
//            //----------------------------------------
//            nonce = new BigInteger(128, rnd).abs();
//            pilaCoin.setNonce(nonce);
//            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
//            //System.out.println(pilaJson);
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.digest(pilaJson.getBytes("UTF-8"));
//            byte[] newHash = md.digest(pilaJson.getBytes("UTF-8"));
//            numHash = new BigInteger(newHash).abs();
//
//            //----------------------------------------
//        } while (dificuldadeStatic.compareTo(numHash) < 0);
//
//        System.out.println(" 🔘 Pilacoin: " + pilaCoin.getNonce());
//        //return pilaCoin;
//
//        //return pilaCoin;
//
//        if (cont == 0) {
//            System.out.println(" 💥 Nao mineirou...");
//        } else {
//            System.out.println(" ✨ Minerou! " + cont + " vezes");
//        }
//    }
//}
