package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.utils.DCTabHandler;
import eu.ecodex.labbox.ui.view.MainLayout;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@UIScope
@org.springframework.stereotype.Component
@RoutePrefix(LabenvLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class LabenvLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    public static final String ROUTE_PREFIX = "labenvironments";
    public static final String TAB_GROUP_NAME = "Lab Environments";

    private final DCTabHandler DCTabHandler = new DCTabHandler();

    private final ApplicationContext applicationContext;

    public LabenvLayout(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
//        this.labenvManager = labenvManager;
//        for (int i = 1; i < labenvManager.getLabenvironments().size(); ++i) {
//            RouteConfiguration.forApplicationScope().setRoute("labenv"+i, LabenvDetails.class, MainLayout.class);
//        }
    }

    @PostConstruct
    void init() {
        DCTabHandler.createTabs(applicationContext, TAB_GROUP_NAME);
        add(DCTabHandler.getTabs());

//        DCTabHandler.createTabReplicas(labenvManager.getLabenvironments());
//        add(DCTabHandler.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        DCTabHandler.beforeEnter(event);
    }
}
