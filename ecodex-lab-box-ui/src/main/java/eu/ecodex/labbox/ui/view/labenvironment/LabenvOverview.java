package eu.ecodex.labbox.ui.view.labenvironment;


import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import eu.ecodex.labbox.ui.view.BaseViewVertical;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = LabenvOverview.ROUTE, layout = LabenvLayout.class)
//@RoleRequired(role = "ADMIN")
public class LabenvOverview extends BaseViewVertical implements BeforeEnterObserver {

    public static final String ROUTE = "";

    public LabenvOverview(UpdateFrontendController updateFrontendController) {
        super(updateFrontendController);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.forwardTo(LabenvSetupListView.class);
    }
}
