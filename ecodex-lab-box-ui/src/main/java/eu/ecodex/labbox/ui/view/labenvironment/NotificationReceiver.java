package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.notification.Notification;

public interface NotificationReceiver {
    void updateAppStateNotification();
    void receiveOneOffNotification(Notification notification);
}
