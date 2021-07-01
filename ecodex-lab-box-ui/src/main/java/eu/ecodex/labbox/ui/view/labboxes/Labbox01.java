package eu.ecodex.labbox.ui.view.labboxes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import org.springframework.stereotype.Component;

@Component
@Route(value = Labbox01.ROUTE, layout = LabboxLayout.class)
@UIScope
@TabMetadata(title = "Labbox 01", tabGroup = LabboxLayout.TAB_GROUP_NAME)
public class Labbox01 extends VerticalLayout {

    public static final String ROUTE = "labbox01";


}
