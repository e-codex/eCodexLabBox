package eu.ecodex.labbox.ui.view.componentdocumentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.view.StaticContentView;

@Component
@UIScope
@Route(value = DomibusGatewayDocumentationView.ROUTE, layout = ComponentDocumentationLayout.class)
@Order(1)
@TabMetadata(title = "DOMIBUS Gateway", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = ComponentDocumentationLayout.TAB_GROUP_NAME)
public class DomibusGatewayDocumentationView extends StaticContentView {

    public static final String ROUTE = "gatewayDocumentation";

    public DomibusGatewayDocumentationView(@Value("${domibus.gateway.web.url}") String urlToDomibusGatewaySite) {
    	super("html/gatewayDocumentation.html");
    	
    	Label txt = new Label("Path to external DOMIBUS site");
    	
    	Anchor link = new Anchor();
    	link.setHref(urlToDomibusGatewaySite);
    	link.setTarget("_blank");
    	link.setTitle(txt.getText());
    	link.add(txt);
    	
    	add(link);
    }
}
