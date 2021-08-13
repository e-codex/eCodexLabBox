package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.utils.DCTabHandler;
import eu.ecodex.labbox.ui.view.labenvironment.LabenvOverview;
import org.springframework.stereotype.Component;

import java.awt.Desktop;

@UIScope
@Component
@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {

    private final DCTabHandler tabManager = new DCTabHandler();

    private final NotificationService notificationService;

    public MainLayout(NotificationService notificationService) {
        this.notificationService = notificationService;

        // TODO migrate this to new notification system when it's done
        if (Desktop.isDesktopSupported()) {
            Notification desktopIntegrationAvailable = Notification.show("Desktop Integration available");
            desktopIntegrationAvailable.setDuration(2000);
            desktopIntegrationAvailable.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            desktopIntegrationAvailable.setPosition(Notification.Position.TOP_END);
        } else {
            Notification noDesktopIntegration = Notification.show(
                    "App is running headless mode and won't work properly. " +
                    "It probably was started with the wrong parameters. " +
                    "Please ensure that the app is run with the .headless(false) option!");
            noDesktopIntegration.addThemeVariants(NotificationVariant.LUMO_ERROR);
            noDesktopIntegration.setPosition(Notification.Position.TOP_END);
            noDesktopIntegration.setDuration(0);
        }

        setPrimarySection(Section.DRAWER);

        VerticalLayout topBar = new VerticalLayout();
        topBar.setWidthFull();
        topBar.add(new DomibusConnectorAdminHeader());
        addToNavbar(topBar);

        tabManager.setTabFontSize("large");
        tabManager
                .createTab()
                .withLabel("Lab Environments")
                .withIcon(new Icon(VaadinIcon.LIST))
                .addForComponent(LabenvOverview.class);

        tabManager
                .createTab()
                .withLabel("Configuration")
                .withIcon(new Icon(VaadinIcon.FILE_CODE));

        tabManager
                .createTab()
                .withLabel("Configuration")
                .withIcon(new Icon(VaadinIcon.COG_O));

        tabManager
                .createTab()
                .withLabel("Info")
                .withIcon(VaadinIcon.INFO_CIRCLE_O);

        tabManager.getTabs().setOrientation(Tabs.Orientation.HORIZONTAL);
        topBar.add(tabManager.getTabs());
    }

    public void beforeEnter(BeforeEnterEvent event) {
        tabManager.beforeEnter(event);
    }
}
