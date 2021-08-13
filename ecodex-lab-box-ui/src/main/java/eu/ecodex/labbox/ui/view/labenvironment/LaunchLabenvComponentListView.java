package eu.ecodex.labbox.ui.view.labenvironment;

import ch.qos.logback.core.util.COWArrayList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.domain.AppStateNotification;
import eu.ecodex.labbox.ui.service.LabenvService;
import eu.ecodex.labbox.ui.service.NotificationService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@UIScope
@Route(value = LaunchLabenvComponentListView.ROUTE, layout = LabenvLayout.class)
@Order(2)
@TabMetadata(title = "Launch", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LaunchLabenvComponentListView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver, ReactiveListUpdates, BroadcastReceiver {

    public static final String ROUTE = "launch";

    private final LabenvService labenvService;
    private final LaunchControlGrid grid;
    private final NotificationService notificationService;
    private final List<Notification> activeNotifications;

    public LaunchLabenvComponentListView(DirectoryController directoryController, LabenvService labenvService, LaunchControlGrid grid, NotificationService notificationService)
    {
        this.labenvService = labenvService;
        this.grid = grid;
        this.notificationService = notificationService;
        directoryController.getReactiveLists().put("launchlist", this);
        directoryController.getBroadcastReceivers().add(this);
        this.activeNotifications = new ArrayList<>();
        notificationService.getProcessedNotifications().clear();

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
    public void updateNotification() {
        getUI().map(ui -> ui.access(() -> {
            notificationService.getNotifications().forEach(asn -> {
                final Set<AppStateNotification> processedNotifications =
                        notificationService.getProcessedNotifications();

                if ( ! processedNotifications.contains(asn)) {
                    final Notification notification = notificationService.createNotification(asn);
                    notification.open();
                    processedNotifications.add(asn);
                }
            });
        }));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(labenvService.getLabenvironments().values());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        notificationService.getNotifications().forEach(asn -> {
            final Set<AppStateNotification> processedNotifications =
                    notificationService.getProcessedNotifications();

            if ( ! processedNotifications.contains(asn)) {
                final Notification notification = notificationService.createNotification(asn);
                notification.open();
                processedNotifications.add(asn);
            }
        });
    }
}
