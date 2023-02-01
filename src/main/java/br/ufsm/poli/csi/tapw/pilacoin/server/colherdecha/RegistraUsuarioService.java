package br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha;

import br.ufsm.poli.csi.tapw.pilacoin.server.model.Usuario;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.UsuarioRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class RegistraUsuarioService {

    @Value("${endereco.server}")
    private String enderecoServer;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    public void init() {
        System.out.println("Registrado usuário: " + registraUsuario("Nathalia"));
    }

    @SneakyThrows
    @Transactional
    public UsuarioRest registraUsuario(String nome) {
        KeyPair keyPair = leKeyPair();
        UsuarioRest usuarioRest = UsuarioRest.builder().nome(nome).chavePublica(keyPair.getPublic().getEncoded()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<UsuarioRest> entity = new HttpEntity<>(usuarioRest, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<UsuarioRest> resp = restTemplate.postForEntity("http://" + enderecoServer + "/usuario/", entity, UsuarioRest.class);
            UsuarioRest usu = resp.getBody();
            Usuario usuarioDB2 = new Usuario();
            assert usu != null;
            usuarioDB2.setNome(usu.nome);
            usuarioDB2.setChavePublica(keyPair.getPublic().getEncoded());
            usuarioDB2.setChavePrivada(keyPair.getPrivate().getEncoded());
            usuarioRepository.save(usuarioDB2);
            return usu;
        } catch (Exception e) {
            System.out.println("usuario já cadastrado.");
            String strPubKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            ResponseEntity<UsuarioRest> resp = restTemplate.postForEntity("http://" + enderecoServer + "/usuario/findByChave", new HttpEntity<>(strPubKey, headers), UsuarioRest.class);
            return resp.getBody();
        }
    }

    @SneakyThrows
    private KeyPair leKeyPair() {
        File fpub = new File("pub.key");
        File fpriv = new File("priv.key");
        if (fpub.exists() && fpriv.exists()) {
            FileInputStream pubIn = new FileInputStream(fpub);
            FileInputStream privIn = new FileInputStream(fpriv);
            byte[] barrPub = new byte[(int) pubIn.getChannel().size()];
            byte[] barrPriv = new byte[(int) privIn.getChannel().size()];
            pubIn.read(barrPub);
            privIn.read(barrPriv);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(barrPub));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(barrPriv));
            return new KeyPair(publicKey, privateKey);
        } else {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.generateKeyPair();
            FileOutputStream pubOut = new FileOutputStream("pub.key", false);
            FileOutputStream privOut = new FileOutputStream("priv.key", false);
            pubOut.write(keyPair.getPublic().getEncoded());
            privOut.write(keyPair.getPrivate().getEncoded());
            pubOut.close();
            privOut.close();
            return keyPair;
        }
    }

    public PublicKey getPublicKey() {
        return leKeyPair().getPublic();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class UsuarioRest {
        private Long id;
        private byte[] chavePublica;
        private String nome;
    }

}
