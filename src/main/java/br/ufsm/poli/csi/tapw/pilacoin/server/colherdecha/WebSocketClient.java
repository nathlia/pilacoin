package br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.model.Bloco;
import br.ufsm.poli.csi.tapw.pilacoin.server.service.ValidaPilaService;
import br.ufsm.poli.csi.tapw.pilacoin.server.service.ValidarBlocoService;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Objects;

@Service
public class WebSocketClient {

    private MyStompSessionHandler sessionHandler = new MyStompSessionHandler();
    @Value("${endereco.server}")
    private String enderecoServer;

    @PostConstruct
    private void init() {
        System.out.println("iniciou");
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connect("ws://" + enderecoServer + "/websocket/websocket", sessionHandler);
        System.out.println("conectou");
    }

    @Scheduled(fixedRate = 3000)
    private void printDificuldade() {
        if (sessionHandler.dificuldade != null) {
            System.out.println("Dificuldade Atual: " + sessionHandler.dificuldade);
        }
    }

    private static class MyStompSessionHandler implements StompSessionHandler {

        private BigInteger dificuldade;
        private PilaCoin pilaCoin;
        private Bloco bloco;

        //topicos que podem ser ouvidos, colocar aqui
        @Override
        public void afterConnected(StompSession stompSession,
                                   StompHeaders stompHeaders)
        {
            stompSession.subscribe("/topic/dificuldade", this);
            stompSession.subscribe("/topic/validaMineracao", this);
            stompSession.subscribe("/topic/descobrirNovoBloco", this);
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        }

        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
        }

        //decobre classe usada para converter
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            //System.out.println(stompHeaders);
            if (Objects.equals(stompHeaders.getDestination(), "/topic/dificuldade")) {
                return DificuldadeRet.class;
            } else if (Objects.equals(stompHeaders.getDestination(), "/topic/validaMineracao")) {
                System.out.println("✨ NEW PILACOINS ARRIVED!");
                return PilaCoin.class;
            } else if (Objects.equals(stompHeaders.getDestination(), "/topic/descobrirNovoBloco")) {
                System.out.println("🎆 NEW BLOCK DISCOVERED!");
                return Bloco.class;
            }
            return null;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            //System.out.println("Received : " + o);
            assert o != null;
            if (Objects.equals(stompHeaders.getDestination(), "/topic/dificuldade")) {
                dificuldade = new BigInteger(((DificuldadeRet) o).getDificuldade(), 16);
            }  else if (o.getClass().equals(PilaCoin.class)) {
                pilaCoin = (PilaCoin) o;
                System.out.println("  ❕  " + pilaCoin);
            } else if (o.getClass().equals(Bloco.class)) {
                bloco = (Bloco) o;
                System.out.println("  ❗  " + bloco);
            }
        }
    }

    public BigInteger getDificuldade() {
        System.out.println(" GET DIFICULDADE : " + sessionHandler.dificuldade);
        return sessionHandler.dificuldade;
    }

    public PilaCoin getPilaCoin() {
        System.out.println(" GET DIFICULDADE : " + sessionHandler.dificuldade);
        return sessionHandler.pilaCoin;
    }
    public Bloco getBloco() {
        System.out.println(" GET DIFICULDADE : " + sessionHandler.dificuldade);
        return sessionHandler.bloco;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DificuldadeRet {
        private String dificuldade;
    }

}
