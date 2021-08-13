package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.AppStateNotification;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.events.*;
import eu.ecodex.labbox.ui.repository.FileAndDirectoryRepo;
import eu.ecodex.labbox.ui.service.*;
import eu.ecodex.labbox.ui.view.labenvironment.BroadcastReceiver;
import eu.ecodex.labbox.ui.view.labenvironment.ReactiveListUpdates;
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
    private final FileAndDirectoryRepo fileAndDirectoryRepo;
    private final WatchDirectoryService watchDirectoryService;
    private final PathMapperService pathMapperService;
    private final LabenvService labenvService;
    private final NotificationService notificationService;
    private final PlatformService platformService;

    @Getter
    private final Map<String, ReactiveListUpdates> reactiveLists;

    @Getter
    private final List<BroadcastReceiver> broadcastReceivers;

    public DirectoryController(WatchDirectoryConfig watchDirectoryConfig, FileAndDirectoryRepo fileAndDirectoryRepo,
                               WatchDirectoryService watchDirectoryService, PathMapperService pathMapperService,
                               LabenvService labenvService, NotificationService notificationService, PlatformService platformService) {
        this.watchDirectoryConfig = watchDirectoryConfig;
        this.fileAndDirectoryRepo = fileAndDirectoryRepo;
        this.watchDirectoryService = watchDirectoryService;
        this.pathMapperService = pathMapperService;
        this.labenvService = labenvService;
        this.notificationService = notificationService;
        this.platformService = platformService;
        this.reactiveLists = new HashMap<>();
        this.broadcastReceivers = new ArrayList<>();
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
    }

    @EventListener
    public void handleNewMavenFolder(CreatedMavenFolderEvent e) {
        // TODO close active "no-maven" notifications
        searchForMaven();
    }

    @EventListener
    public void handleDeletedMavenFolder(DeletedMavenFolderEvent event) {
        fileAndDirectoryRepo.setMavenExecutable(Optional.empty());
        notificationService.getNotifications().add(AppStateNotification.NO_MAVEN);
        broadcastReceivers.forEach(BroadcastReceiver::updateNotification);
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
        // toUIPublisher.publishEvent(event);

//        LabenvSetupListView setuplist = (LabenvSetupListView) reactiveUiComponents.get("setuplist");
//        setuplist.updateList();
        reactiveLists.forEach((k, v) -> v.updateList());
    }

    @EventListener
    public void handleDeletedLabenvFolder(DeletedLabenvFolderEvent e) {
        Path full = pathMapperService.getFullPath(e.getNameOfDeletedDirectory());

        labenvService.getLabenvironments().remove(full);

        reactiveLists.forEach((k, v) -> v.updateList());
    }

    public synchronized Path getLabenvHomeDirectory() {
        return fileAndDirectoryRepo.getLabenvHomeDirectory();
    }

    public void startMonitoring() {
        watchDirectoryService.startMonitoring();
    }

    public void stopMonitoring() {
        watchDirectoryService.stopMonitoring();
    }

    public synchronized void setLabenvHomeDirectory(Path path) {
        stopMonitoring();
        fileAndDirectoryRepo.setLabenvHomeDirectory(path);
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
        startMonitoring();
        searchForLabenvDirectories();
        searchForMaven();
        reactiveLists.forEach((k, v) -> v.updateList());
    }

    // note: this runs once on startup
    public void searchForLabenvDirectories() {

        try {
            labenvService.setLabenvironments(
                    Files.list(fileAndDirectoryRepo.getLabenvHomeDirectory())
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

    // note: this runs once on startup
    public Optional<Path> searchForMaven() {
        Optional<Path> mvn = Optional.empty();
        try {
            mvn = Files.list(fileAndDirectoryRepo.getLabenvHomeDirectory())
                    .filter(Files::isDirectory)
                    .filter(d -> d.getFileName().toString().startsWith("apache-maven"))
                    .map(d -> d.resolve("bin"))
                    .map(d -> {
                        if (platformService.isWindows()) {
                            return d.resolve("mvn_cmd");
                        } else {
                            return d.resolve("mvn");
                        }
                    })
                    .findFirst();
            fileAndDirectoryRepo.setMavenExecutable(mvn);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Logging
        }
        if (!mvn.isPresent()) {
            notificationService.getNotifications().add(AppStateNotification.NO_MAVEN);
            broadcastReceivers.forEach(BroadcastReceiver::updateNotification);
        }
        return mvn;
    }
}
