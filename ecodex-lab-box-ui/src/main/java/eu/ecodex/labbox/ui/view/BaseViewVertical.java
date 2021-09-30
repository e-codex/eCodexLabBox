package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import eu.ecodex.labbox.ui.domain.AppState;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;

import java.util.Map;
import java.util.Set;

// This abstraction was created to help with app state notifications / system alerts
// if a app state is broadcasted all views check if they are the active ui and then check if they need to show a
// notification. This lead to the code in updateAppStateNotification() to be duplicalted in all views. That's why
// I created this base class, because all views should be able to receive the alert, otherwise the notification might
// go unnoticed. Use BaseViewVertical wherever you would extend VerticalLayout.
// If you should ever need a view with horizontal layout or other vaadin component, just mimic what's done here.
public class BaseViewVertical extends VerticalLayout implements NotificationReceiver {

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
                    if (notification != null) {
                        notification.setOpened(false);
                    }
                }
            }
        }));
    }
}
