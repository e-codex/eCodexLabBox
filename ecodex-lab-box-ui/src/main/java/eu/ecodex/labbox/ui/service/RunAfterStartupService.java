package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.controller.DirectoryController;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class RunAfterStartupService {

    final DirectoryController directoryController;

    public RunAfterStartupService(DirectoryController directoryController) {
        this.directoryController = directoryController;
    }

    // ContextRefreshedEvent event -> when context is completely initialized
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        directoryController.searchForLabenvDirectories();
        directoryController.startMonitoring();
        directoryController.searchForMaven();
        directoryController.searchForLab();
    }
}
