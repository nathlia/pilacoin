package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilaCoinRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PilaCoinService {
    @Value("${endereco.server}")
    private String enderecoServer;

    @Autowired
    private PilaCoinRepository pilaCoinRepository;
    private PilaCoin pilaCoinReceived;

    @Transactional
    public void getPilacoinAndSend(PilaCoin pilaCoin){
        pilaCoinReceived = pilaCoin;
        sendPilacoin();
    }

    @Transactional
    public void savePila(PilaCoin pilaCoin){
        PilaCoin pilaSaved = pilaCoinRepository.save(pilaCoin);
        pilaSaved.setIdCriador("Nathalia");
        if (pilaSaved.getId() != null) {
            System.out.println(" 💾 Pila saved: " + pilaSaved.getId());
        } else {
            System.out.println(" ❌ ERROR Saving pila");
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    @PostConstruct
    public void sendPilacoin() {
        postPilacoin(pilaCoinReceived);
    }

    @SneakyThrows
    @Transactional
    public void postPilacoin2(PilaCoin pilaCoin) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PilaCoin> resp = null;
        try {

            String pilaJson = objectMapper.writeValueAsString(pilaCoin);
            System.out.println(pilaJson);
            System.out.println("--------------------------------------------------------------------------------------------");

            RequestEntity<String> requestEntity = RequestEntity.post(new URL(
                            "http://srv-ceesp.proj.ufsm.br:8097/pilacoin/").toURI())
                    .contentType(MediaType.APPLICATION_JSON).body(pilaJson);

            resp = restTemplate.exchange(requestEntity, PilaCoin.class);


            if (resp.getStatusCode() == HttpStatus.OK) {
                System.out.println("📤 PILACOIN SENT TO SERVER! :\n " + pilaJson);
                savePila(pilaCoin);
            }

        } catch (Exception e) {
            System.out.println(" ❌ ERROR SENDING PILACOIN TO SERVER! ❌ ");
            System.out.println(" " + e.getMessage());
            System.out.println("--------------------------------------------------------------------------------------------");
        }
    }

    public void postPilacoin(PilaCoin pilaCoin) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PilaCoin> resp = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String pilaJson = objectMapper.writeValueAsString(pilaCoin);
            System.out.println(pilaJson);
            System.out.println("--------------------------------------------------------------------------------------------");

            HttpEntity<String> entity = new HttpEntity<String>(pilaJson, headers);

            resp = restTemplate.postForEntity("http://srv-ceesp.proj.ufsm.br:8097/pilacoin/", entity, PilaCoin.class);
            PilaCoin sentPila = resp.getBody();

            if (resp.getStatusCode() == HttpStatus.OK) {
                assert sentPila != null;
                System.out.println(" 📤 Pilacoin sent to server successfully: " + sentPila.toString());
            }

        } catch (Exception e) {
            System.out.println(" ❌ Error sending pilacoin to server: " + e.getMessage());
        }
    }

    public boolean isNonceValid(PilaCoin pilaCoin, BigInteger targetDifficulty) throws JsonProcessingException {
        PilaCoin pilaCoinToValidade = new PilaCoin();

        pilaCoinToValidade.setId(pilaCoin.getId());
        pilaCoinToValidade.setIdCriador(pilaCoin.getIdCriador());
        pilaCoinToValidade.setChaveCriador(pilaCoin.getChaveCriador());
        pilaCoinToValidade.setAssinaturaMaster(pilaCoin.getAssinaturaMaster());
        pilaCoinToValidade.setDataCriacao(pilaCoin.getDataCriacao());
        pilaCoinToValidade.setStatus(pilaCoin.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String nonce = String.valueOf(pilaCoin.getNonce());
        String data =  objectMapper.writeValueAsString(pilaCoinToValidade);
        byte[] hash = getHash(data + nonce);

        // Convert the hash to a BigInteger for comparison with the target difficulty
        BigInteger hashInt = new BigInteger(1, hash);

        // Compare the hash with the target difficulty
        return hashInt.compareTo(targetDifficulty) < 0;
    }

    private byte[] getHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PilaCoinToSend {
        private String dificuldade;
    }

}
