package eu.ecodex.labbox.ui.controller;

import com.vaadin.flow.component.Component;
import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.events.CreatedLabenvFolderEvent;
import eu.ecodex.labbox.ui.domain.events.DeletedLabenvFolderEvent;
import eu.ecodex.labbox.ui.domain.events.LabenvBuildSucceeded;
import eu.ecodex.labbox.ui.service.LabenvService;
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
    private final LabenvService labenvService;

    @Getter
    private final Map<String, Component> reactiveUiComponents;


    public DirectoryController(WatchDirectoryConfig watchDirectoryConfig, WatchDirectoryService watchDirectoryService, PathMapperService pathMapperService, LabenvService labenvService) {
        this.watchDirectoryConfig = watchDirectoryConfig;
        this.watchDirectoryService = watchDirectoryService;
        this.pathMapperService = pathMapperService;
        this.labenvService = labenvService;
        this.reactiveUiComponents = new HashMap<>();
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
    }

    @EventListener
    public void handleLabenvBuildSucceeded(LabenvBuildSucceeded e) {
        final Labenv labenv = labenvService.getLabenvironments().get(e.getFullPathToLabenv());
        labenv.parseGatewayServerXml();
        labenv.parseConnectorProperties();
        labenv.parseGatewayServerXml();
    }

    @EventListener
    public void handleNewLabenvFolder(CreatedLabenvFolderEvent e) {
        Path full = pathMapperService.getFullPath(e.getNameOfNewDirectory());

        // TODO how to handle the case, that later on properties are added???
        Labenv newLabenv = Labenv.buildOnly(full);
        labenvService.getLabenvironments().put(full, newLabenv);

        // Adding a spring event listener to a vaadin view causes threading problems
        //toUIPublisher.publishEvent(event);

        // if user has not visited LabenvListView then this will be null
        LabenvListView listlabs = (LabenvListView) reactiveUiComponents.get("listlabs");
        listlabs.updateList();
    }

    @EventListener
    public void handleDeletedLabenvFolder(DeletedLabenvFolderEvent e) {
        Path full = pathMapperService.getFullPath(e.getNameOfDeletedDirectory());

        labenvService.getLabenvironments().remove(full);

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

    // note: this runs once on startup
    public void scanForLabDirectories() {

        try {
            labenvService.setLabenvironments(
                    Files.list(watchDirectoryConfig.getLabenvHomeDirectory())
                        .filter(Files::isDirectory)
                        .filter(d -> d.getFileName().toString().startsWith("labenv"))
                        .collect(toMap(path -> path, Labenv::buildAndParse))
            );
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Logging
        }
        // TODO warn user if a folder does not contain step I) anything II) important files
    }
}
