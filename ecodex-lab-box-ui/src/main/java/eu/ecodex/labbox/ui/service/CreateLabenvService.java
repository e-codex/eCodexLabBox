package eu.ecodex.labbox.ui.service;

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
    //
    @Async
    public void createNextLabenv(ProcessBuilder processBuilder, Path fullPath) {
        Process start = null;
        try {
            start = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (start.isAlive()) {
            System.out.println("waiting");
        }
        System.out.println(start.exitValue());

        applicationEventPublisher.publishEvent(new LabenvBuildSucceeded(this, fullPath));
    }
}
