package br.ufsm.poli.csi.tapw.pilacoin.server.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha.RegistraUsuarioService;
import br.ufsm.poli.csi.tapw.pilacoin.server.model.Bloco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.PublicKey;
import java.util.Date;

@Service
public class ValidarBlocoService {

    @Autowired
    private RegistraUsuarioService registraUsuarioService;

    public void validarBloco(Bloco bloco) {
        PublicKey publicKey = registraUsuarioService.getPublicKey();

        System.out.println(" ============================================================================================");
        System.out.println("||                                  START: BLOCO                                        ||");
        System.out.println(" ============================================================================================");

        while (true) {



            System.out.println(" ============================================================================================");
            System.out.println("||                                  FINISH: BLOCO                                       ||");
            System.out.println(" ============================================================================================");

        }
    }
}
