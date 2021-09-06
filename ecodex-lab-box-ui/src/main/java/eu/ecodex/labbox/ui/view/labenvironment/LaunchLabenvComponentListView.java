package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.domain.AppState;
import eu.ecodex.labbox.ui.service.LabenvService;
import eu.ecodex.labbox.ui.service.NotificationService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@UIScope
@Route(value = LaunchLabenvComponentListView.ROUTE, layout = LabenvLayout.class)
@Order(2)
@TabMetadata(title = "Launch", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LaunchLabenvComponentListView extends VerticalLayout implements AfterNavigationObserver, ReactiveListUpdates, BroadcastReceiver {

    public static final String ROUTE = "launch";

    private final LabenvService labenvService;
    private final LaunchControlGrid grid;
    private final NotificationService notificationService;

    public LaunchLabenvComponentListView(DirectoryController directoryController, LabenvService labenvService, LaunchControlGrid grid, NotificationService notificationService)
    {
        this.notificationService = notificationService;
        this.labenvService = labenvService;
        this.grid = grid;
        directoryController.getReactiveLists().put("launchlist", this);
        directoryController.getBroadcastReceivers().add(this);

        final VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.add(grid);

        final VerticalLayout main = new VerticalLayout();
        main.add(gridLayout);

        add(main);
    }

    @Override
    public void updateList() {
        // if user has not visited this view then getUI() will be null
        // that's why we .map instead of using .get, map won't do anything if null is passed
        getUI().map(ui -> ui.access(() -> {
            grid.setItems(labenvService.getLabenvironments().values());
        }));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(labenvService.getLabenvironments().values());
        updateAppStateNotification();
    }

    // accessing the UI here isn't possible, because it is not yet available !
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        updateAppStateNotification();
//    }

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
