package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.PilaCoinRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;

@Service
public class MineracaoService {

    @Autowired
    private MineracaoController mineracaoController = new MineracaoController();

    @Autowired
    PilaCoinRepository pilaCoinRepository;

    @SneakyThrows
    public void initPilacoint(boolean minerar) {

        RegistraUsuarioService registraUsuarioService = new RegistraUsuarioService();
        PublicKey publicKey = registraUsuarioService.getPublicKey();

        while (minerar) {
            SecureRandom rnd = new SecureRandom();

            PilaCoin pilaCoin = new PilaCoin();
            pilaCoin.setDataCriacao(new Date());
            pilaCoin.setChaveCriador(publicKey.getEncoded());
            pilaCoin.setNonce(new BigInteger(128, rnd).abs());
            pilaCoin.setIdCriador("Nathalia");

            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.digest(pilaJson.getBytes("UTF-8"));
            byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));

            Minerador(hash, pilaCoin);
        }
    }

    @SneakyThrows
    private void Minerador(byte[] hash, PilaCoin pilaCoin) {
        //1329227995784915872903807060280344575
        BigInteger nonce = pilaCoin.getNonce();

        BigInteger dificuldade = mineracaoController.getDificuldade();

        BigInteger numHash = new BigInteger(hash).abs();
//        BigInteger dificuldadeStatic = new BigInteger("1766847064778384329583297500742918515827483896875618958121606201292619775").abs();
        SecureRandom rnd = new SecureRandom();

        // gerar nonce
        // setar pilacoin
        // converte string
        // gera hash pilacoin
        // comparar hash com a dificuldade = minerado

        do {
            //----------------------------------------
            nonce = new BigInteger(128, rnd).abs();
            pilaCoin.setNonce(nonce);
            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.digest(pilaJson.getBytes("UTF-8"));
            byte[] newHash = md.digest(pilaJson.getBytes("UTF-8"));
            numHash = new BigInteger(newHash).abs();
            //----------------------------------------
        } while (dificuldade.compareTo(numHash) < 0);

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println(" 🔘 Pilacoin: ");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println(" ✔ = " + numHash);
        System.out.println(" 💥 = " + dificuldade);
        System.out.println(" nonce: " + pilaCoin.getNonce());
        System.out.println(" compare: " + dificuldade.compareTo(numHash));
        System.out.println("--------------------------------------------------------------------------------------------");

        PilaCoin pilaSaved = pilaCoinRepository.save(pilaCoin);

        if (pilaSaved.getId() != null) {
            System.out.println("Pila saved: " + pilaSaved.getId());
        } else {
            System.out.println("Falha ao salvar pila na base de dados");
        }

    }


}



