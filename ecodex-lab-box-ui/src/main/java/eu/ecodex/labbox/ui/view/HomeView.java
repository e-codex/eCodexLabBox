package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.domain.AppState;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.view.labenvironment.BroadcastReceiver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@UIScope
@Route(value = HomeView.ROUTE, layout = MainLayout.class)
public class HomeView extends VerticalLayout implements BroadcastReceiver, AfterNavigationObserver {
    public static final String ROUTE = "";

    private final NotificationService notificationService;

    Label l = new Label();

    public HomeView(NotificationService notificationService) {
        this.notificationService = notificationService;

        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("95%");
        horizontalLayout.setHeightFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        horizontalLayout.add(l);

        l.setText("Welcome to Lab-box UI");
        l.getStyle().set("font-size", "large");

        add(horizontalLayout);
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

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateAppStateNotification();
    }
    // accessing the UI here isn't possible, because it is not yet available !
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        updateAppStateNotification();
//    }
}
