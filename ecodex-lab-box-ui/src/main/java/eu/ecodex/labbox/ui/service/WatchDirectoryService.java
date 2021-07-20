package eu.ecodex.labbox.ui.service;

import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

@Service
public class WatchDirectoryService {

    public Set<Path> paths = new HashSet<>();
    private Path folder;

    @Async
    public void watchFilesystem() {
        java.nio.file.WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            // TODO Logging
            e.printStackTrace();
        }

        try {
            folder.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            // TODO Logging
            e.printStackTrace();
        }

        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {

                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");

                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_CREATE.name())) {

//                        CreatedLabboxInFilesystemEvent testEvent = new CreatedLabboxInFilesystemEvent(this, "testtesttestes");
//                        applicationEventPublisher.publishEvent(testEvent);

                        paths.add(folder.resolveSibling(event.context().toString()));
                    }
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_DELETE.name())) {
                        paths.remove(folder.resolveSibling(event.context().toString()));
                    }
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {

                    }
                }
                key.reset();
            }

        } catch (InterruptedException e) {
            // TODO Logging
            e.printStackTrace();
        }
    }
}
