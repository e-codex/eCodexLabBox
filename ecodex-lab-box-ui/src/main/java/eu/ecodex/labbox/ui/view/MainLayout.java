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
import eu.ecodex.labbox.ui.utils.DCTabHandler;
import eu.ecodex.labbox.ui.view.labenvironment.LabenvOverview;
import org.springframework.stereotype.Component;

import java.awt.*;

@UIScope
@Component
@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {

    private final DCTabHandler tabManager = new DCTabHandler();

    public MainLayout() {

        // just a test
        if (Desktop.isDesktopSupported()) {
            Notification notification = Notification.show("Desktop Integration available");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.TOP_END);
        } else {
            // won't be seen anyway, because there is no display in headless mode
            Notification notification = Notification.show("App is running headless! No Desktop support!");
            // TODO match with other notifications
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_END);
        }

        setPrimarySection(Section.DRAWER);

        VerticalLayout topBar = new VerticalLayout();
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
