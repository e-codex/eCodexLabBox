package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.domain.Exitcode;
import eu.ecodex.labbox.ui.domain.events.LabenvBuildFailed;
import eu.ecodex.labbox.ui.domain.events.LabenvBuildSucceeded;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class CreateLabenvService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public CreateLabenvService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // this controls whether the ui stalls while building the lab env or not
    @Async
    public void createNextLabenv(ProcessBuilder processBuilder, Path fullPath) {
        Process run = run(processBuilder);
        while (run.isAlive()) ; // blocks execution until process is finished
        if (run.exitValue() == Exitcode.SCRIPT_SUCCESS.value) {
            applicationEventPublisher.publishEvent(new LabenvBuildSucceeded(this, fullPath));
//        } else if (run.exitValue() == Exitcode.PROXY_CONNECTION_ERROR.value) {
//            applicationEventPublisher.publishEvent(new LabenvBuildFailed(this, Exitcode.PROXY_CONNECTION_ERROR, fullPath));
        } else {
            applicationEventPublisher.publishEvent(new LabenvBuildFailed(this, Exitcode.getExitcodeByValue(run.exitValue()), fullPath));
        }
    }

    private Process run(ProcessBuilder processBuilder) {
        Process p = null;
        try {
            p = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
}
