package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.domain.AppStateNotification;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.view.labenvironment.BroadcastReceiver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@UIScope
@Route(value = HomeView.ROUTE, layout = MainLayout.class)
public class HomeView extends VerticalLayout implements BroadcastReceiver, BeforeEnterObserver {
    public static final String ROUTE = "";

    private final NotificationService notificationService;

    private List<Notification> activeNotifications;

    Label l = new Label();

    public HomeView(NotificationService notificationService) {
        this.notificationService = notificationService;
        activeNotifications = new ArrayList<>();
        notificationService.getProcessedNotifications().clear();

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
