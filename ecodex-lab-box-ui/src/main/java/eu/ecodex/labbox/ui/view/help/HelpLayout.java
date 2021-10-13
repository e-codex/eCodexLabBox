package eu.ecodex.labbox.ui.view.help;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.utils.DCTabHandler;
import eu.ecodex.labbox.ui.view.MainLayout;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@UIScope
@org.springframework.stereotype.Component
@RoutePrefix(HelpLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class HelpLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    public static final String ROUTE_PREFIX = "help";
    public static final String TAB_GROUP_NAME = "Help";

    private final DCTabHandler DCTabHandler = new DCTabHandler();

    private final ApplicationContext applicationContext;

    public HelpLayout(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
//        setWidth("95%");
        setSizeFull();

        DCTabHandler.createTabs(applicationContext, TAB_GROUP_NAME);
        add(DCTabHandler.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        DCTabHandler.beforeEnter(event);
    }
}
