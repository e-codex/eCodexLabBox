package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

@UIScope
@Component
@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {

    private Tabs tabs;
    private DCTabHandler tabManager = new DCTabHandler();

    public MainLayout() {

        setPrimarySection(Section.DRAWER);

        VerticalLayout topBar = new VerticalLayout();
        topBar.add(new DomibusConnectorAdminHeader());
        addToNavbar(topBar);

        tabManager.setTabFontSize("bigger");
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

        tabs = tabManager.getTabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        topBar.add(tabs);
    }

    public void beforeEnter(BeforeEnterEvent event) {
        tabManager.beforeEnter(event);
    }
}
