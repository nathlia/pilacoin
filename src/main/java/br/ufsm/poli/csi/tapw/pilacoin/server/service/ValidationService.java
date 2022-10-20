package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Date;

@Service
public class ValidationService {

    @SneakyThrows
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PilaCoin pilaCoin = PilaCoin.builder()
                .dataCriacao(new Date())
                .idCriador("professor")
                .chaveCriador(kp.getPublic().getEncoded())
                .magicNumber(new BigInteger(128, new SecureRandom()))
                .assinaturaMaster("fdgfdsgfdsgfdsgdsfgdsf".getBytes(StandardCharsets.UTF_8)).build();
        System.out.println(mapper.writeValueAsString(pilaCoin));
    }

}
