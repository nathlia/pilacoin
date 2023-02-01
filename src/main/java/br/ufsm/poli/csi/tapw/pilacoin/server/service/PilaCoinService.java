package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.net.URL;

@Service
public class PilaCoinService {
    @Value("${endereco.server}")
    private String enderecoServer;

    private String pilaJsonReceived;

    @Transactional
    public void getPilacoinAndSend(String pilaJson){
        pilaJsonReceived = pilaJson;
        sendPilacoin();
    }


    @PostConstruct
    public void sendPilacoin() {
        postPilacoin(pilaJsonReceived);
    }

    @SneakyThrows
    @Transactional
    public void postPilacoin(String pilaJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PilaCoin> resp = null;
        try {

            RequestEntity<String> requestEntity = RequestEntity.post(new URL(
                            "http://"+ "srv-ceesp.proj.ufsm.br:8097" + "/pilacoin/").toURI())
                    .contentType(MediaType.APPLICATION_JSON).body(pilaJson);

            resp = restTemplate.exchange(requestEntity, PilaCoin.class);

//            ResponseEntity<PilaCoin> resp = restTemplate.postForEntity("http://" + "srv-ceesp.proj.ufsm.br:8097" + "/pilacoin/", entity, PilaCoin.class);
//            PilaCoin sentPila = resp.getBody();
//            assert sentPila != null;
            if (resp.getStatusCode() == HttpStatus.OK) {
                System.out.println("📤 PILACOIN SENT SERVER! :\n " + pilaJson);
            }

        } catch (Exception e) {
            System.out.println(" ❌ ERROR SENDING PILACOIN TO SERVER! ❌ ");
            System.out.println(" " + e.getMessage());
        }

    }

}
