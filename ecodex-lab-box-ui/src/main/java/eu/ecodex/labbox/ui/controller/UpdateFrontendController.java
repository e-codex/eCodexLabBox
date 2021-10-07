package eu.ecodex.labbox.ui.controller;

import com.vaadin.flow.component.notification.Notification;
import eu.ecodex.labbox.ui.domain.AppState;
import eu.ecodex.labbox.ui.service.UpdateFrontendService;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import eu.ecodex.labbox.ui.view.labenvironment.ReactiveListUpdates;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;

@Controller
public class UpdateFrontendController {

    private final UpdateFrontendService updateFrontendService;

    public UpdateFrontendController(UpdateFrontendService updateFrontendService) {
        this.updateFrontendService = updateFrontendService;
    }

    public Set<ReactiveListUpdates> getListOfViewsWithLiveUpdates() {
        return updateFrontendService.getReactiveLists();
    }

    public Set<NotificationReceiver> getNotificationReceivers() {
        return updateFrontendService.getNotificationReceivers();
    }

    public Set<AppState> getAppState() {
        return updateFrontendService.getAppState();
    }

    public Map<String, Notification> getActiveNotifications() {
        return updateFrontendService.getActiveNotifications();
    }

    public Notification getNotification(AppState state) {
        return updateFrontendService.createNotification(state);
    }
}
