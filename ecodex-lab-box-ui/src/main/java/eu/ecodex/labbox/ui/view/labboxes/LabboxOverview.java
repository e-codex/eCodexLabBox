package eu.ecodex.labbox.ui.view.labboxes;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = LabboxOverview.ROUTE, layout = LabboxLayout.class)
//@RoleRequired(role = "ADMIN")
public class LabboxOverview extends VerticalLayout implements BeforeEnterObserver {

    public static final String ROUTE = "";

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
//        event.forwardTo(Some.class);
    }
}
