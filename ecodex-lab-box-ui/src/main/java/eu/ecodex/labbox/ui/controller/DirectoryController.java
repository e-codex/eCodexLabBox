package eu.ecodex.labbox.ui.controller;

import com.vaadin.flow.component.Component;
import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.events.CreatedLabenvFolderEvent;
import eu.ecodex.labbox.ui.domain.events.DeletedLabenvFolderEvent;
import eu.ecodex.labbox.ui.service.PathMapperService;
import eu.ecodex.labbox.ui.service.WatchDirectoryService;
import eu.ecodex.labbox.ui.view.labenvironment.LabenvListView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.util.stream.Collectors.toMap;


@Slf4j
@Controller
public class DirectoryController {

    private final WatchDirectoryConfig watchDirectoryConfig;
    private final WatchDirectoryService watchDirectoryService;
    private final PathMapperService pathMapperService;

    @Getter
    private final Map<String, Component> reactiveUiComponents;

    @Getter
    private Map<Path, Labenv> labenvironments;

    public DirectoryController(WatchDirectoryConfig watchDirectoryConfig, WatchDirectoryService watchDirectoryService, PathMapperService pathMapperService) {
        this.watchDirectoryConfig = watchDirectoryConfig;
        this.watchDirectoryService = watchDirectoryService;
        this.pathMapperService = pathMapperService;
        this.reactiveUiComponents = new HashMap<>();
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
        this.labenvironments = new HashMap<>();
    }

    @EventListener
    public void handleNewLabenvFolder(CreatedLabenvFolderEvent e) {
        Path full = pathMapperService.getFullPath(e.getNameOfNewDirectory());

        Labenv newLabenv = new Labenv(full);
        labenvironments.put(full, newLabenv);

        // Adding a spring event listener to a vaadin view causes threading problems
        //toUIPublisher.publishEvent(event);

        // if user has not visited LabenvListView then this will be null
        LabenvListView listlabs = (LabenvListView) reactiveUiComponents.get("listlabs");
        listlabs.updateList();
    }

    @EventListener
    public void handleDeletedLabenvFolder(DeletedLabenvFolderEvent e) {
        Path full = pathMapperService.getFullPath(e.getNameOfDeletedDirectory());

        labenvironments.remove(full);

        LabenvListView listlabs = (LabenvListView) reactiveUiComponents.get("listlabs");
        listlabs.updateList();
    }

    public Path getLabenvHomeDirectory() {
        return watchDirectoryConfig.getLabenvHomeDirectory();
    }

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
        // TODO warn user if a folder does not contain step I) anything II) important files
    }
}
