package eu.ecodex.labbox.ui.view.help;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import eu.ecodex.labbox.ui.view.BaseViewVertical;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = HelpView.ROUTE, layout = HelpLayout.class)
@Order(1)
@TabMetadata(title = "Tutorial", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = HelpLayout.TAB_GROUP_NAME)
public class HelpView extends BaseViewVertical implements AfterNavigationObserver, NotificationReceiver {

    public static final String ROUTE = "tutorial";

    public HelpView(UpdateFrontendController updateFrontendController) {
        super(updateFrontendController);
        
        IFrame tutorial = new IFrame("html/tutorial.html");
//        tutorial.setWidth("100%");
        tutorial.setHeight("1000px");
        tutorial.setSizeFull();
        tutorial.getStyle().set("margin", "0");
        tutorial.getStyle().set("padding", "0");
        tutorial.getStyle().set("border", "none");
        
        add(tutorial);
        
        setHeight("1000px");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateAppStateNotification();
    }
}
