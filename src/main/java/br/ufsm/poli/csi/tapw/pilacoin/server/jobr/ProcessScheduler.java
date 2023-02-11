package br.ufsm.poli.csi.tapw.pilacoin.server.jobr;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.poli.csi.tapw.pilacoin.server.controller.MineracaoController;
import br.ufsm.poli.csi.tapw.pilacoin.server.service.MineracaoService;
import br.ufsm.poli.csi.tapw.pilacoin.server.service.ValidaPilaService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessScheduler {
    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private MineracaoService mineracaoService;

    @Autowired
    private MineracaoController mineracaoController;

    @Autowired
    private ValidaPilaService validaPilaService;

    @Job(name = "Mineracao de pila")
    public void processOnce(boolean minerar) {
        jobScheduler.enqueue(() -> mineracaoService.initPilacoint(minerar));
    }

    @Job(name = "Salvar Pilacoins do Colega")
    public void salvarPilasDoColega() {
        System.out.println("START SAVING INCOMING PILACOINS! ");
        jobScheduler.scheduleRecurrently(Cron.minutely(), () -> validaPilaService.salvaPilaDoColega());
    }

    @Job(name = "Mineracao de pila")
    public void processRecurrently(boolean minerar) {
        jobScheduler.scheduleRecurrently(Cron.minutely(), () -> mineracaoService.initPilacoint(minerar));
    }

}
