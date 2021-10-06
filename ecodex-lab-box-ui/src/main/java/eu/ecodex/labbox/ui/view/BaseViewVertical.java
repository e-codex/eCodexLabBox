package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import eu.ecodex.labbox.ui.domain.AppState;
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

    private final UpdateFrontendController updateFrontendController;

    public BaseViewVertical(UpdateFrontendController updateFrontendController) {
        this.updateFrontendController = updateFrontendController;
    }

    @Override
    public void updateAppStateNotification() {
        // Checks all defined app states and activates or deactivates the associated notification.
        // Activation happens only if the message isn't currently displayed (active)
        // To further complicate this, the user might have multiple tabs open.
        // We want to show the notification in every tab, but never twice in each tab.
        // That's why the key is a composite of UIId and AppState.
        getUI().map(ui -> ui.access(() -> {
            final Set<AppState> appState = updateFrontendController.getAppState();
            for (AppState state : AppState.values()) {
                final Map<String, Notification> activeNotifications = updateFrontendController.getActiveNotifications();
                if (appState.contains(state)) {
                    if (!activeNotifications.containsKey(state.toString() + ui.getUIId())) {
                        final Notification notification = updateFrontendController.getNotification(state);
                        activeNotifications.put(state.toString() + ui.getUIId(), notification);
                        notification.open();
                    }
                } else {
                    final Notification notification = activeNotifications.remove(state.toString() + ui.getUIId());
                    if (notification != null) {
                        notification.setOpened(false);
                    }
                }
            }
        }));
    }
}
