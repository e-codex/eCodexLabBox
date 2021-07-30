package eu.ecodex.labbox.ui.configuration;

import eu.ecodex.labbox.ui.AppStarter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

@Slf4j
@Configuration
public class WatchDirectoryConfig {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Getter
    private Path labenvHomeDirectory;

    public synchronized boolean setLabenvHomeDirectory(String path) {
        File file = new File(path);
        if (file.exists()) {
            this.labenvHomeDirectory = file.toPath();
            return true;
        }
        return false;
    }

    public synchronized void setLabenvHomeDirectory(Path path) {
        this.labenvHomeDirectory = path;
    }

    WatchDirectoryConfig() {
        // TODO set default location only, if database/property is not set, remove throws
        try {
            this.labenvHomeDirectory = new File(AppStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
        } catch (URISyntaxException e) {
            log.debug("unable to set default monitoring folder");
        }
    }

    public WatchService watchService() {
        log.debug("MONITORING_FOLDER: {}", labenvHomeDirectory);
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();

            if (!Files.isDirectory(labenvHomeDirectory)) {
                throw new RuntimeException("incorrect monitoring folder: " + labenvHomeDirectory);
            }

            labenvHomeDirectory.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
        } catch (IOException e) {
            log.error("exception for watch service creation:", e);
        }
        return watchService;

    }
}
