package br.ufsm.poli.csi.tapw.pilacoin.server.controller;


import br.ufsm.poli.csi.tapw.pilacoin.server.jobr.ProcessScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class PilacoinController {
    @Autowired
    ProcessScheduler processScheduler;

    @GetMapping("/minerar")
    public void scheduleRecurrently() {
        boolean minerar = true;
        processScheduler.processOnce(minerar);
    }


}
