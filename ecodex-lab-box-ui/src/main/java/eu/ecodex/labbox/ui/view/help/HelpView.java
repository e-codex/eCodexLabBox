package eu.ecodex.labbox.ui.view.help;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = HelpView.ROUTE, layout = HelpLayout.class)
@Order(1)
@TabMetadata(title = "Tutorial", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = HelpLayout.TAB_GROUP_NAME)
public class HelpView extends VerticalLayout {

    public static final String ROUTE = "tutorial";

    public HelpView() {

        IFrame tutorial = new IFrame("html/tutorial.html");
        tutorial.setWidth("100%");

        add(tutorial);
    }
}
