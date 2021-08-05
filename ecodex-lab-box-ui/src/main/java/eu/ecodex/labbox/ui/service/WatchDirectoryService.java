package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.domain.events.CreatedLabenvFolderEvent;
import eu.ecodex.labbox.ui.domain.events.DeletedLabenvFolderEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Service
public class WatchDirectoryService {

    ApplicationEventPublisher applicationEventPublisher;

    WatchDirectoryService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Getter
    @Setter
    WatchService watchService;

    @Async
    public void startMonitoring() {
        log.info("START_MONITORING");
        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {

                    // TODO comment out
                    // For debugging:
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");

                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_CREATE.name())) {
                        if (event.context().toString().startsWith("labenv")) {
                            CreatedLabenvFolderEvent c = new CreatedLabenvFolderEvent(this, event.context().toString());
                            applicationEventPublisher.publishEvent(c);
                        }
                    }
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_DELETE.name())) {
                        if (event.context().toString().startsWith("labenv")) {
                            DeletedLabenvFolderEvent c = new DeletedLabenvFolderEvent(this, event.context().toString());
                            applicationEventPublisher.publishEvent(c);
                        }
                    }
                }
                key.reset();
            }
        } catch (ClosedWatchServiceException expected) {
            // Throws: ClosedWatchServiceException – if this watch service is closed OR closed while waiting for the next key
            log.info("restarting watcher in different directory");
        } catch (InterruptedException e) {
            log.warn("interrupted exception for monitoring service");
        }
    }

    @PreDestroy
    public void stopMonitoring() {
        log.info("STOP_MONITORING");

        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                log.error("exception while closing the monitoring service");
            }
        }
    }
}
