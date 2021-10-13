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
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.view.StaticContentView;

@Component
@UIScope
@Route(value = DomibusConnectorDocumentationView.ROUTE, layout = ComponentDocumentationLayout.class)
@Order(2)
@TabMetadata(title = "domibusConnector", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = ComponentDocumentationLayout.TAB_GROUP_NAME)
public class DomibusConnectorDocumentationView extends StaticContentView {

    public static final String ROUTE = "connectorDocumentation";
    
    
    public DomibusConnectorDocumentationView(DirectoryController directoryController, @Value("${path.to.connector.documentation.site}") String pathToConnectorDocumentationSite) {
    	super("html/connectorDocumentation.html");
    	
    	Label pathToDocumentation = new Label();
    	pathToDocumentation.setText(directoryController.getLabenvHomeDirectory().toString() + pathToConnectorDocumentationSite);
    	
    	add(pathToDocumentation);
    }

  
}
