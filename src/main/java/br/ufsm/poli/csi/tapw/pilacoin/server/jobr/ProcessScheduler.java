package br.ufsm.poli.csi.tapw.pilacoin.server.jobr;

import br.ufsm.poli.csi.tapw.pilacoin.server.service.MineracaoService;
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

    @Job
    public void processOnce(boolean minerar) {
        jobScheduler.enqueue(() -> mineracaoService.initPilacoint(minerar));
    }

    @Job(name = "Mineracao de pila")
    public void processRecurrently(boolean minerar) {
        jobScheduler.scheduleRecurrently(Cron.minutely(), () -> mineracaoService.initPilacoint(minerar));
    }

}
