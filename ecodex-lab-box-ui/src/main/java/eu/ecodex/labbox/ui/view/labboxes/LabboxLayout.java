package eu.ecodex.labbox.ui.view.labboxes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.utils.DCTabHandler;
import eu.ecodex.labbox.ui.view.MainLayout;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@UIScope
@org.springframework.stereotype.Component
@RoutePrefix(LabboxLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class LabboxLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    public static final String ROUTE_PREFIX = "labbox";
    public static final String TAB_GROUP_NAME = "Labbox";

    private final DCTabHandler DCTabHandler = new DCTabHandler();
    private final ApplicationContext applicationContext;

    public LabboxLayout(ApplicationContext applicationContext )
    {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
        DCTabHandler.createTabs(applicationContext, TAB_GROUP_NAME);
        add(DCTabHandler.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }
}
