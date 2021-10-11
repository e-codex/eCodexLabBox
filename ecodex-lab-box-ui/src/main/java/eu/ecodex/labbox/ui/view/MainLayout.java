package eu.ecodex.labbox.ui.view;

import java.awt.Desktop;

import org.springframework.stereotype.Component;

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

import eu.ecodex.labbox.ui.service.UpdateFrontendService;
import eu.ecodex.labbox.ui.utils.DCTabHandler;
import eu.ecodex.labbox.ui.view.componentdocumentation.DomibusGatewayDocumentationView;
import eu.ecodex.labbox.ui.view.help.HelpView;
import eu.ecodex.labbox.ui.view.labenvironment.LabenvOverview;
import eu.ecodex.labbox.ui.view.settings.ProxySettingsView;

@UIScope
@Component
@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {

    private final DCTabHandler tabManager = new DCTabHandler();

    public MainLayout(UpdateFrontendService updateFrontendService) {

        // if we were only using the map instead if a set and a map, then we would have to set the values of the map
        // to null here, in other words, only clearing the active notifications associated with an app state
        updateFrontendService.getActiveNotifications().clear();

        // migrate this to new notification system when it's done
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
                .withLabel("Settings")
                .withIcon(new Icon(VaadinIcon.SCREWDRIVER))
                .addForComponent(ProxySettingsView.class);

        tabManager
                .createTab()
                .withLabel("Component Documentation")
                .withIcon(new Icon(VaadinIcon.RECORDS))
                .addForComponent(DomibusGatewayDocumentationView.class);

        tabManager
                .createTab()
                .withLabel("Help")
                .withIcon(new Icon(VaadinIcon.QUESTION_CIRCLE_O))
                .addForComponent(HelpView.class);

        tabManager.getTabs().setOrientation(Tabs.Orientation.HORIZONTAL);
        topBar.add(tabManager.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tabManager.beforeEnter(event);
    }
}
