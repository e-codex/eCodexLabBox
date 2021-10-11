package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.domain.AppEventState;
import eu.ecodex.labbox.ui.domain.AppEventType;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.events.*;
import eu.ecodex.labbox.ui.repository.FileAndDirectoryRepo;
import eu.ecodex.labbox.ui.service.LabenvService;
import eu.ecodex.labbox.ui.service.PathMapperService;
import eu.ecodex.labbox.ui.service.UpdateFrontendService;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import eu.ecodex.labbox.ui.view.labenvironment.ReactiveListUpdates;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.util.Optional;

@Controller
public class EventHandlingController {

    private final LabenvService labenvService;
    private final PathMapperService pathMapperService;
    private final FileAndDirectoryRepo fileAndDirectoryRepo;
    private final UpdateFrontendService updateFrontendService;

    public EventHandlingController(LabenvService labenvService, PathMapperService pathMapperService, FileAndDirectoryRepo fileAndDirectoryRepo, UpdateFrontendService updateFrontendService) {
        this.labenvService = labenvService;
        this.pathMapperService = pathMapperService;
        this.fileAndDirectoryRepo = fileAndDirectoryRepo;
        this.updateFrontendService = updateFrontendService;
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

    // TODO delete ?
    @EventListener
    public void handleDeletedMavenFolder(DeletedMavenFolderEvent event) {
        fileAndDirectoryRepo.setMavenExecutable(Optional.empty());
        updateFrontendService.getAppEventState().add(new AppEventState(AppEventType.NO_MAVEN));
        updateFrontendService.getNotificationReceivers().forEach(NotificationReceiver::updateAppStateNotification);
    }

    @EventListener
    public void handleLabenvBuildSucceeded(LabenvBuildSucceeded e) {
        final Labenv labenv = labenvService.getLabenvironments().get(e.getFullPathToLabenv());
        labenv.parseGatewayServerXml();
        labenv.parseConnectorProperties();
        labenv.parseClientProperties();
        final AppEventState appEventState = new AppEventState(AppEventType.LABENV_CREATED);
        appEventState.getMetaData().setPath(e.getFullPathToLabenv());
        updateFrontendService.getAppEventState().add(appEventState);
        updateFrontendService.getNotificationReceivers().forEach(NotificationReceiver::updateAppStateNotification);
    }

    @EventListener
    public void handleLabenvBuildFailed(LabenvBuildFailed e) {
        final AppEventState appEventState = new AppEventState(AppEventType.LABENV_BUILD_FAILED);
        appEventState.getMetaData().setExitcode(e.getExitcode());
        appEventState.getMetaData().setPath(e.getFullPath());
        updateFrontendService.getAppEventState().add(appEventState);
        updateFrontendService.getNotificationReceivers().forEach(NotificationReceiver::updateAppStateNotification);
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
        updateFrontendService.getReactiveLists().forEach(ReactiveListUpdates::updateList);
    }

    @EventListener
    public void handleDeletedLabenvFolder(DeletedLabenvFolderEvent e) {
        Path full = pathMapperService.getFullPath(e.getNameOfDeletedDirectory());

        labenvService.getLabenvironments().remove(full);

        updateFrontendService.getReactiveLists().forEach(ReactiveListUpdates::updateList);
    }
}
