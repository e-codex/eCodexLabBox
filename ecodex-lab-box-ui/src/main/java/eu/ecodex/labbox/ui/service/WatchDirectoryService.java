package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.domain.events.CreatedLabboxInFilesystemEvent;
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

                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");

                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_CREATE.name())) {

                        CreatedLabboxInFilesystemEvent testEvent = new CreatedLabboxInFilesystemEvent(this, "testtesttestes");
                        applicationEventPublisher.publishEvent(testEvent);

                        // TODO I think I need to handle this elsewhere and pass the relevant data in the event
                        // otherwise I end in cyclic dependencies between this and the service that uses this
                        // this component just creates the events and the service starts, stops the monitoring
                        // and listens to the events

//                        Labenv l = Labenv.builder().path(watchDirectoryConfig.getLabenvHomeDirectory().resolveSibling(event.context().toString())).build();
//                        labenvironments.add(l);


                    }
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_DELETE.name())) {
//                        Labenv l = Labenv.builder().path(watchDirectoryConfig.getLabenvHomeDirectory().resolveSibling(event.context().toString())).build();
//                        labenvironments.remove(l);
                    }
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {
                        // TODO delete this branch
                        // I think this is only used when permissions are changed or so, renaming is just a delete and create
                    }

                }
                key.reset();
            }
        } catch (ClosedWatchServiceException expected) {
//            Throws:
//            ClosedWatchServiceException – if this watch service is closed, or it is closed while waiting for the next key
            log.debug("restarting watcher in different directory");
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
