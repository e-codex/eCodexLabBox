package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

// TODO Maybe Notification Receiver would be a better name?
public interface BroadcastReceiver {
    void updateAppStateNotification();
}
