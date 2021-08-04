package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.service.WatchDirectoryService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;


@Slf4j
@Controller
public class DirectoryController {

    private final WatchDirectoryConfig watchDirectoryConfig;
    private final WatchDirectoryService watchDirectoryService;

    // TODO listen for events from directory watcher

    @Getter
    private Map<Path, Labenv> labenvironments;

    public DirectoryController(WatchDirectoryConfig watchDirectoryConfig, WatchDirectoryService watchDirectoryService) {
        this.watchDirectoryConfig = watchDirectoryConfig;
        this.watchDirectoryService = watchDirectoryService;
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
        this.labenvironments = new HashMap<>();
    }

    public Path getLabenvHomeDirectory() {
        return watchDirectoryConfig.getLabenvHomeDirectory();
    }
//    public void setLabenvHomeDirectory(String path) {
//        stopMonitoring();
//        watchDirectoryConfig.setLabenvHomeDirectory(path);
//        watchService = watchDirectoryConfig.watchService();
//        launchMonitoring();
//    }

    public void startMonitoring() {
        watchDirectoryService.startMonitoring();
    }

    public void stopMonitoring() {
        watchDirectoryService.stopMonitoring();
    }

    public void setLabenvHomeDirectory(Path path) {
        stopMonitoring();
        watchDirectoryConfig.setLabenvHomeDirectory(path);
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
        startMonitoring();
    }

    // run once on startup
    public void scanForLabDirectories() {

        try {
            labenvironments = Files.list(watchDirectoryConfig.getLabenvHomeDirectory())
                    .filter(Files::isDirectory)
                    .filter(d -> d.getFileName().toString().startsWith("labenv"))
                    .collect(toMap(path -> path, Labenv::new));
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Logging
        }

        // TODO warn if a folder does not contain step I) anything II) important files

    }


}
