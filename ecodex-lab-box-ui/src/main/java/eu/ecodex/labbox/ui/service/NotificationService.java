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
import eu.ecodex.labbox.ui.repository.AppStateRepo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class NotificationService {

    private final AppStateRepo appStateRepo;

    public NotificationService(AppStateRepo appStateRepo) {
        this.appStateRepo = appStateRepo;
    }

    public synchronized Map<AppState, Notification> getActiveNotifications() {
        return appStateRepo.getActiveNotifications();
    }

    public synchronized Set<AppState> getAppState() {
        return appStateRepo.getAppState();
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
                    // TODO replace with something more appropriate
                    UI.getCurrent().getPage().open("https://maven.apache.org/download.cgi?Preferred=ftp://ftp.osuosl.org/pub/apache/");
                    notification.close();
                    appStateRepo.getActiveNotifications().remove(AppState.NO_MAVEN);
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
                    appStateRepo.getActiveNotifications().remove(AppState.NO_MAVEN);
                });
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        return closeBtn;
    }

    public Notification createNotification(AppState appStateNotification) {
        if (appStateNotification == AppState.NO_MAVEN) {
            return createNoMavenNotification();
        } else {
            // this ensures all types are handled here
            throw new IllegalStateException("Bad Notification Type!");
        }
    }
}
