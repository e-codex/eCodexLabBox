package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.domain.AppState;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.view.labenvironment.BroadcastReceiver;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

// All views should receive system alerts, in order to
@UIScope
@Component
public class BaseViewVertical extends VerticalLayout implements BroadcastReceiver {

    private final NotificationService notificationService;

    public BaseViewVertical(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void updateAppStateNotification() {
        getUI().map(ui -> ui.access(() -> {
            // checks all defined app states and activates or deactivates the associated notification.
            // Activation happens only if the message isn't currently displayed (active)
            final Set<AppState> appState = notificationService.getAppState();
            for (AppState s : AppState.values()) {
                final Map<AppState, Notification> activeNotifications = notificationService.getActiveNotifications();
                if (appState.contains(s)) {
                    if (!activeNotifications.containsKey(s)) {
                        final Notification notification = notificationService.createNotification(AppState.NO_MAVEN);
                        activeNotifications.put(AppState.NO_MAVEN, notification);
                        notification.open();
                    }
                } else {
                    final Notification notification = activeNotifications.remove(AppState.NO_MAVEN);
                    notification.setOpened(false);
                }
            }
        }));
    }
}
