package eu.ecodex.labbox.ui.view.componentdocumentation;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = ComponentDocumentationView.ROUTE, layout = ComponentDocumentationLayout.class)
@Order(1)
@TabMetadata(title = "ComponentDocumentation1", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = ComponentDocumentationLayout.TAB_GROUP_NAME)
public class ComponentDocumentationView extends VerticalLayout {

    public static final String ROUTE = "component-documentation-1";

}
