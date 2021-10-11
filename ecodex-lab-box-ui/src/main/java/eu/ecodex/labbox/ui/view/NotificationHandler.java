package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import eu.ecodex.labbox.ui.domain.AppEventState;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UIScope
@Component
public class NotificationHandler extends VerticalLayout implements NotificationReceiver {

    private final UpdateFrontendController updateFrontendController;

    @Getter
    private Map<AppEventState, Notification> activeNotifications;

    public NotificationHandler(UpdateFrontendController updateFrontendController) {
        this.updateFrontendController = updateFrontendController;
        this.activeNotifications = new HashMap<>();
        updateFrontendController.getNotificationReceivers().add(this);
    }

    @Override
    public void updateAppStateNotification() {
        getUI().map(ui -> ui.access(() -> {
            // refresh App State
            final Set<AppEventState> appState = updateFrontendController.getAppState();
            // create new notifications or transfer if already active
            final Map<AppEventState, Notification> newActiveNotifications = appState.stream()
                    .map(s ->{
                        if (activeNotifications.containsKey(s)) {
                            return new AbstractMap.SimpleEntry<>(s, activeNotifications.remove(s));
                        } else {
                            Notification newNotification = updateFrontendController.getNotification(s);
                            newNotification.open();
                            return new AbstractMap.SimpleEntry<>(s, newNotification);
                        }
                    })
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
            // close remaining
            activeNotifications.values().forEach(Notification::close);
            // set new active state
            activeNotifications = newActiveNotifications;
        }));
    }

    @Override
    public void receiveOneOffNotification(Notification notification) {

    }
}
