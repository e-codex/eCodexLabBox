package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.Labenv;
import eu.ecodex.labbox.ui.service.WatchDirectoryService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Controller
public class DirectoryController {

    WatchDirectoryConfig watchDirectoryConfig;
    WatchDirectoryService watchDirectoryService;

    // TODO listen for events from

    @Getter
    private final Set<Labenv> labenvironments;

    public DirectoryController(WatchDirectoryConfig watchDirectoryConfig, WatchDirectoryService watchDirectoryService) {
        this.watchDirectoryConfig = watchDirectoryConfig;
        this.watchDirectoryService = watchDirectoryService;
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
        this.labenvironments = new HashSet<>();
//        new Thread(DirectoryService::launchMonitoring).start();
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
        watchDirectoryService.launchMonitoring();
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

        // TODO this still has bugs

        labenvironments.clear();

        List<Path> labenvPaths = new ArrayList<>();
        try {
            labenvPaths = Files.list(watchDirectoryConfig.getLabenvHomeDirectory())
                    .filter(d -> d.getFileName().toString().startsWith("labenv"))
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // TODO Logging and user notification
        }

        for (Path labenvPath : labenvPaths) {
            String[] nameAndNumberOfDirectory = labenvPath.getFileName().toString().split("labenv");

            if (nameAndNumberOfDirectory.length > 0) {
                //skip if
//                final String s = nameAndNumberOfDirectory[1];
//                int parse = 0;
//                try {
//                    parse = Integer.parseInt(s);
//                } catch (NumberFormatException ignored) {}
//                if (parse == i+1) {
//                    continue;
//                }

                int labcount = 1;
                boolean success = false;
                while (!success) {
                    try {
                        Files.move(labenvPath, labenvPath.resolveSibling("labenv" + labcount++));
                        success = true;
                    } catch (FileAlreadyExistsException ignore) {
                    } catch (IOException e) {
                        // TODO logging and user notifications
                    }
                }
                Labenv l = Labenv.builder().id(labcount).path(labenvPath).build();
                labenvironments.add(l);
            }
        }
    }


}
