package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.WebSocketClient;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilaCoinRepository;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;


public class Minerar {
    @Autowired
    private WebSocketClient webSocketClient;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PilaCoinRepository pilaCoinRepository;
    @Autowired
    private MineracaoController mineracaoController;


//    @Scheduled(fixedRate = 30000)
//    private void enviaMsgDificuldade() {
//        webSocketClient.convertAndSend("/topic/dificuldade", 10000);
//    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        System.out.println("-------- INSIDE PILA -------------");
        System.out.println(event);
        this.initPilacoint();
    }

    @EventListener
    @SneakyThrows
    public void initPilacoint() {
        String path = "C:\\Users\\aluno\\Downloads\\pilacoin-rest_nathalia";
//
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//        kpg.initialize(2048);

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

//        comparar hash, transforma em numero
//        BigInteger numHash = new BigInteger(hash).abs();
//        if (numHash.compareTo(BigInteger.valueOf(100000000)) < 0) {
//            System.out.println(" ✨ Minerou!");
//        } else {
//            System.out.println(" 💥 Nao mineirou...");
//        }

//        comparar hash, transforma em numero
//        BigInteger numHash = new BigInteger(hash).abs();
//        if (numHash.compareTo(BigInteger.valueOf(100000000)) < 0) {
//         System.out.println(" ✨ Minerou!");
//        } else {
//            System.out.println(" 💥 Nao mineirou...");
//        }

        Minerador(hash, pilaCoin);
        //System.out.println("-------- Topico Conectado -------------");
//        Message<byte[]> message = event.getMessage();
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        StompCommand command = accessor.getCommand();
//        if (command.equals(StompCommand.SUBSCRIBE)) {
//            System.out.println("------------- Topico Inscrito ----------------");
//            String sessionId = accessor.getSessionId();
//            String stompSubscriptionId = accessor.getSubscriptionId();
//            String destination = accessor.getDestination();
//            System.out.println("sessionid= " + sessionId + ", stomSubsID= " + stompSubscriptionId + ", destination= " + destination);
//        }
    }

    @SneakyThrows
    public PilaCoin Minerador(byte[] hash, PilaCoin pilaCoin) {
        //1329227995784915872903807060280344575
        BigInteger nonce = pilaCoin.getNonce();

        BigInteger dificuldade = mineracaoController.getDificuldade();
        System.out.println(dificuldade);
        BigInteger numHash = new BigInteger(hash).abs();
        //BigInteger dificuldadeStatic = new BigInteger("132922799578491587290380756456456456456456456456456456406234123123344575").abs();
        SecureRandom rnd = new SecureRandom();

        // gerar nonce
        // setar pilacoin
        // converte string
        // gera hash pilacoin
        // comparar hash com a dificuldade = minerado

        int cont = 0;
        int tentativas = 0;
        while (numHash.compareTo(dificuldade) > 0) {
            //coin
            if (numHash.compareTo(dificuldade) < 0) {
                System.out.println(" 🔘 Pilacoin: " + pilaCoin);
                cont++;
                return pilaCoin;
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
            System.out.println("loop:" + tentativas);
        }

        if (cont == 0) {
            System.out.println(" 💥 Nao mineirou...");
        } else {
            System.out.println(" ✨ Minerou! " + cont + " vezes");
        }
        return pilaCoin;
    }
}
