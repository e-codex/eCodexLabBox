package eu.ecodex.labbox.ui.service;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import eu.ecodex.labbox.ui.domain.AppState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class NotificationService {

    private final Set<AppState> appState;
    private final Map<AppState, Notification> activeNotifications;

    public NotificationService() {
        this.appState = new HashSet<>();
        this.activeNotifications = new HashMap<>();
    }

    public synchronized Map<AppState, Notification> getActiveNotifications() {
        return this.activeNotifications;
    }

    public synchronized Set<AppState> getAppState() {
        return this.appState;
    }

    private Notification createNoMavenNotification() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_END);
        notification.setId(AppState.NO_MAVEN.toString());

        Icon icon = VaadinIcon.WARNING.create();
        Div info = new Div(new Text("No maven distribution found!"));

        Button retryBtn = new Button(
                "Download",
                clickEvent -> {
                    UI.getCurrent().getPage().open("https://maven.apache.org/download.cgi?Preferred=ftp://ftp.osuosl.org/pub/apache/");
                    notification.close();
                }
        );
        retryBtn.getStyle().set("margin", "0 0 0 var(--lumo-space-m)");

        HorizontalLayout layout = new HorizontalLayout(
                icon, info, retryBtn,
                createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);
        return notification;
    }

    private Button createCloseBtn(Notification notification) {
        Button closeBtn = new Button(
                VaadinIcon.CLOSE_SMALL.create(),
                clickEvent -> {
                    notification.close();
                    activeNotifications.remove(AppState.NO_MAVEN);
                });
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        return closeBtn;
    }

    // if you just store ui notifications in a list and try to open them you run into the problem
    // that the notifications themselves are stateful -> remember whether they were opened before
    // after refresh the notification is not opened again
    // Since our notifications are a system state indicator we need to persist across a refresh
    // that's why we provide fresh instances through this matching method
    public Notification createNotification(AppState appStateNotification) {
        if (appStateNotification == AppState.NO_MAVEN) {
            return createNoMavenNotification();
        } else {
            throw new IllegalStateException("Bad Notification Type!");
        }
    }
}
