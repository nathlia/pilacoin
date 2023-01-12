package br.ufsm.poli.csi.tapw.pilacoin.server.jobr;

import br.ufsm.poli.csi.tapw.pilacoin.server.service.Mineracao;
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
    private Mineracao mineracao;

    @Job
    public void processOnce(boolean minerar) {
        jobScheduler.enqueue(() -> mineracao.initPilacoint(minerar));
    }

    @Job(name = "Mineracao de pila")
    public void processScheduledRecurrently(boolean minerar) {
        jobScheduler.scheduleRecurrently(Cron.minutely(), () -> mineracao.initPilacoint(minerar));
    }

}
