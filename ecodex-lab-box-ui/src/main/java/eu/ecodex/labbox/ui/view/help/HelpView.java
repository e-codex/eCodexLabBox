package eu.ecodex.labbox.ui.view.help;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.view.StaticContentView;

@Component
@UIScope
@Route(value = HelpView.ROUTE, layout = HelpLayout.class)
@Order(1)
@TabMetadata(title = "Tutorial", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = HelpLayout.TAB_GROUP_NAME)
public class HelpView extends StaticContentView {

    public static final String ROUTE = "tutorial";

    public HelpView() {
        super("html/tutorial.html");
    }

}
