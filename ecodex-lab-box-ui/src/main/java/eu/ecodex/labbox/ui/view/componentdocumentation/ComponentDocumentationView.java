package eu.ecodex.labbox.ui.view.componentdocumentation;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import eu.ecodex.labbox.ui.view.BaseViewVertical;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = ComponentDocumentationView.ROUTE, layout = ComponentDocumentationLayout.class)
@Order(1)
@TabMetadata(title = "ComponentDocumentation1", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = ComponentDocumentationLayout.TAB_GROUP_NAME)
public class ComponentDocumentationView extends BaseViewVertical implements AfterNavigationObserver, NotificationReceiver {

    public static final String ROUTE = "component-documentation-1";

    public ComponentDocumentationView(UpdateFrontendController updateFrontendController) {
        super(updateFrontendController);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }
}
