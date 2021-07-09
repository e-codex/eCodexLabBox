package eu.ecodex.labbox.ui.view.labenvironment;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = LabenvOverview.ROUTE, layout = LabenvLayout.class)
//@RoleRequired(role = "ADMIN")
public class LabenvOverview extends VerticalLayout implements BeforeEnterObserver {

    public static final String ROUTE = "";

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
//        event.forwardTo(Some.class);
    }
}
