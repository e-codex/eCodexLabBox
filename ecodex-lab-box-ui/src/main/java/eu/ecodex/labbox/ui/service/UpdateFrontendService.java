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
import eu.ecodex.labbox.ui.domain.AppEventState;
import eu.ecodex.labbox.ui.domain.AppEventType;
import eu.ecodex.labbox.ui.repository.AppEventStateRepo;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import eu.ecodex.labbox.ui.view.labenvironment.ReactiveListUpdates;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UpdateFrontendService {

    private final AppEventStateRepo appEventStateRepo;

    @Getter
    private final Set<ReactiveListUpdates> reactiveLists;

    @Getter
    private final Set<NotificationReceiver> notificationReceivers;

    public UpdateFrontendService(AppEventStateRepo appEventStateRepo) {
        this.appEventStateRepo = appEventStateRepo;
        this.reactiveLists = new HashSet<>();
        this.notificationReceivers = new HashSet<>();
    }

    public synchronized Map<String, Notification> getActiveNotifications() {
        return appEventStateRepo.getActiveNotifications();
    }

    public synchronized Set<AppEventState> getAppEventState() {
        return appEventStateRepo.getAppEventState();
    }

    private Notification createNoMavenNotification() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_END);
        notification.setId(AppEventType.NO_MAVEN.toString());

        Icon icon = VaadinIcon.WARNING.create();
        Div info = new Div(new Text("No maven distribution found!"));

        Button retryBtn = new Button(
                "Download",
                clickEvent -> {
                    // TODO replace with something more appropriate
                    UI.getCurrent().getPage().open("https://maven.apache.org/download.cgi?Preferred=ftp://ftp.osuosl.org/pub/apache/");
                    notification.close();
                    appEventStateRepo.getActiveNotifications().remove(UI.getCurrent().getUIId() + AppEventType.NO_MAVEN.toString());
                }
        );
        retryBtn.getStyle().set("margin", "0 0 0 var(--lumo-space-m)");

        Button closeBtn = new Button(
                VaadinIcon.CLOSE_SMALL.create(),
                clickEvent -> {
                    notification.close();
                    appEventStateRepo.getActiveNotifications().remove(UI.getCurrent().getUIId() + AppEventType.NO_MAVEN.toString());
                });
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        HorizontalLayout layout = new HorizontalLayout(
                icon, info, retryBtn,
                closeBtn);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);
        return notification;
    }

    private Notification createBuildFailedNotification(AppEventState appEventState) {
        final Notification notification = new Notification();
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        final Icon icon = VaadinIcon.WARNING.create();
        final Text text = new Text(
                MessageFormat.format("Building {0} failed, reason: {1}",
                appEventState.getMetaData().getPath().getFileName(),
                appEventState.getMetaData().getExitcode().text)
        );
        Div info = new Div(text);
        Button closeBtn = new Button(
                VaadinIcon.CLOSE_SMALL.create(),
                clickEvent -> {
                    notification.close();
//                    appEventStateRepo.getActiveNotifications().remove(UI.getCurrent().getUIId() + AppEventState.LABENV_CREATED.toString());
                    appEventStateRepo.getAppEventState().remove(appEventState);
                    notificationReceivers.forEach(NotificationReceiver::updateAppStateNotification);
                });
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        HorizontalLayout layout = new HorizontalLayout(
                icon, info, closeBtn);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);
        return notification;
    }

    public Notification createNotification(AppEventState state) {
        if (state.getAppEventType() == AppEventType.NO_MAVEN) {
            return createNoMavenNotification();
        } else if (state.getAppEventType() == AppEventType.LABENV_CREATED) {
            return createLabenvCreatedRememberPmodeConfigNotification(state);
        } else if (state.getAppEventType() == AppEventType.LABENV_BUILD_FAILED) {
            return createBuildFailedNotification(state);
        } else {
            // this ensures all types are handled here
            throw new IllegalStateException("Bad Notification Type!");
        }
    }

    private Notification createLabenvCreatedRememberPmodeConfigNotification(AppEventState state) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setId(state.getAppEventType().toString());

        Icon icon = VaadinIcon.WARNING.create();
        final Text text = new Text("New Lab Environment built successfully! \nDon't forget to upload the labenv's pmodes to the gateway! \nMore details can be found in the tutorial.");
        Div info = new Div(text);

        Button confirmedButton = new Button(
                "Confirm",
                clickEvent -> {
                    notification.close();
//                    appEventStateRepo.getActiveNotifications().remove(UI.getCurrent().getUIId() + state.getAppEventType().toString() + state.getMetaData().getPath().getFileName());
                    appEventStateRepo.getAppEventState().remove(state);
                    notificationReceivers.forEach(NotificationReceiver::updateAppStateNotification);
                }
        );
        confirmedButton.getStyle().set("margin", "0 0 0 var(--lumo-space-l)");
        confirmedButton.setMinWidth("15%");

        HorizontalLayout layout = new HorizontalLayout(
                icon, info, confirmedButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);
        return notification;
    }
}
