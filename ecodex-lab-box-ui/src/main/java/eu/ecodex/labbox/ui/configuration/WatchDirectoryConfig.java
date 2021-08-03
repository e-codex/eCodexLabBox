package eu.ecodex.labbox.ui.configuration;

import eu.ecodex.labbox.ui.AppStarter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

@Slf4j
@Configuration
public class WatchDirectoryConfig {

    private final ApplicationEventPublisher publisher;

    @Getter
    private Path labenvHomeDirectory;

    public WatchDirectoryConfig(ApplicationEventPublisher publisher,
                                @Value("${labenvironments.homedir}") String path)
    {
        this.publisher = publisher;

        // if the app can't set the labhome based on the property string it
        // sets the working directory to wherever the app was started from
        if (!(path != null && setLabenvHomeDirectory(path)))
        {
            try {
                this.labenvHomeDirectory = new File(AppStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
            } catch (URISyntaxException e) {
                log.debug("unable to set default monitoring folder");
            }
        }
    }

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
