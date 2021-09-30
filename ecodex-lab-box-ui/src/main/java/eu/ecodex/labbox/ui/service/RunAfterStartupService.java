package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.controller.DirectoryController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class RunAfterStartupService {

    final DirectoryController directoryController;
    final String port;

    public RunAfterStartupService(DirectoryController directoryController,  @Value("${server.port}") String port) {
        this.directoryController = directoryController;
        this.port = port;
    }

    // ContextRefreshedEvent event -> when context is completely initialized
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        directoryController.searchForLabenvDirectories();
        directoryController.startMonitoring();
        directoryController.searchForMaven();
        directoryController.searchForLab();
    }

    @EventListener
    public void openBrowser(ApplicationReadyEvent event) {
        String url = "http://localhost:" + port;
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
