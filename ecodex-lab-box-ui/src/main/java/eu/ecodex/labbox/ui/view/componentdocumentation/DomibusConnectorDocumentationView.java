package eu.ecodex.labbox.ui.view.componentdocumentation;

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
    
    private final DirectoryController directoryController;

    public DomibusConnectorDocumentationView(DirectoryController directoryController) {
    	super("html/connectorDocumentation.html");
    	
    	this.directoryController = directoryController;
    	
    	Label pathToDocumentation = new Label();
    	pathToDocumentation.setText(directoryController.getConnectorDocuSitePath().toString());
    	
    	Anchor link = new Anchor();
    	link.setHref("file:///"+directoryController.getConnectorDocuSitePath().toString());
    	link.setTarget("_blank");
    	link.setTitle(pathToDocumentation.getText());
    	link.add(pathToDocumentation);
    	
    	add(link);
    }

  
}
