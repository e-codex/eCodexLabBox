package eu.ecodex.labbox.ui.view.componentdocumentation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.ecodex.labbox.ui.configuration.TabMetadata;

@Component
@UIScope
@Route(value = DomibusConnectorClientDocumentationView.ROUTE, layout = ComponentDocumentationLayout.class)
@Order(3)
@TabMetadata(title = "domibusConnectorClient", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = ComponentDocumentationLayout.TAB_GROUP_NAME)
public class DomibusConnectorClientDocumentationView extends VerticalLayout {

    public static final String ROUTE = "connectorClientDocumentation";

    public DomibusConnectorClientDocumentationView() {
    }
}
