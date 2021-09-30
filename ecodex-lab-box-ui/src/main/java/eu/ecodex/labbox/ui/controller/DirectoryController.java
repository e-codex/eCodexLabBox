package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.AppState;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.events.*;
import eu.ecodex.labbox.ui.repository.FileAndDirectoryRepo;
import eu.ecodex.labbox.ui.service.*;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import eu.ecodex.labbox.ui.view.labenvironment.ReactiveListUpdates;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.util.stream.Collectors.toMap;


@Slf4j
@Controller
public class DirectoryController {

    private final String mode;

    private final WatchDirectoryConfig watchDirectoryConfig;
    private final FileAndDirectoryRepo fileAndDirectoryRepo;
    private final WatchDirectoryService watchDirectoryService;
    private final PathMapperService pathMapperService;
    private final LabenvService labenvService;
    private final NotificationService notificationService;
    private final PlatformService platformService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Getter
    private final Map<String, ReactiveListUpdates> reactiveLists;

    @Getter
    private final List<NotificationReceiver> broadcastReceivers;

    public DirectoryController(@Value("${spring.profiles.active}") String mode, WatchDirectoryConfig watchDirectoryConfig, FileAndDirectoryRepo fileAndDirectoryRepo,
                               WatchDirectoryService watchDirectoryService, PathMapperService pathMapperService,
                               LabenvService labenvService, NotificationService notificationService, PlatformService platformService, ApplicationEventPublisher applicationEventPublisher) {
        this.mode = mode;
        this.watchDirectoryConfig = watchDirectoryConfig;
        this.fileAndDirectoryRepo = fileAndDirectoryRepo;
        this.watchDirectoryService = watchDirectoryService;
        this.pathMapperService = pathMapperService;
        this.labenvService = labenvService;
        this.notificationService = notificationService;
        this.platformService = platformService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.reactiveLists = new HashMap<>();
        this.broadcastReceivers = new ArrayList<>();
        watchDirectoryService.setWatchService(watchDirectoryConfig.watchService());
    }

    @EventListener
    public void handleDeletedLabFolder(DeletedLabFolderEvent e) {
        labenvService.setLab(null);
    }

    @EventListener
    public void handleLabBuildSucceeded(CreatedLabFolderEvent e) {
        Path fullPath = pathMapperService.getFullPath(e.getNameOfNewDirectory());
        labenvService.setLab(fullPath);
    }

    @EventListener
    public void handleNewMavenFolder(CreatedMavenFolderEvent e) {
        searchForMaven();
        broadcastReceivers.forEach(NotificationReceiver::updateAppStateNotification);
    }

    @EventListener
    public void handleDeletedMavenFolder(DeletedMavenFolderEvent event) {
        fileAndDirectoryRepo.setMavenExecutable(Optional.empty());
        notificationService.getAppState().add(AppState.NO_MAVEN);
        broadcastReceivers.forEach(NotificationReceiver::updateAppStateNotification);
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

        // how to handle the case, that later on properties are added???
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
        reactiveLists.forEach((k, v) -> v.updateList());
        searchForMaven();
        broadcastReceivers.forEach(NotificationReceiver::updateAppStateNotification);
        searchForLab();
    }

    // runs on startup
    public void searchForLab() {
        try {
            labenvService.setLab(
                    Files.list(fileAndDirectoryRepo.getLabenvHomeDirectory())
                            .filter(Files::isDirectory)
                            .filter(d -> d.getFileName().toString().equals("lab"))
                            .findFirst()
                            .orElse(null)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // runs on startup
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
        }
    }

    // note: this runs once on startup
    public Optional<Path> searchForMaven() {
        Optional<Path> mvn = Optional.empty();
        if (mode.equals("prod")) {
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mvn = Optional.of(fileAndDirectoryRepo.findExecutableOnPath("mvn"));
        }

        fileAndDirectoryRepo.setMavenExecutable(mvn);

        // TODO migrate to AppStateService
        final Set<AppState> notifications = notificationService.getAppState();
        if (!mvn.isPresent()) {
            notifications.add(AppState.NO_MAVEN);
        } else {
            notifications.remove(AppState.NO_MAVEN);
        }
        return mvn;
    }
}
