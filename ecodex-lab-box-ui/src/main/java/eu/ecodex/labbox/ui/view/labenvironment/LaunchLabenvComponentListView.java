package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = LaunchLabenvComponentListView.ROUTE, layout = LabenvLayout.class)
@Order(2)
@TabMetadata(title = "Launch", icon = VaadinIcon.ROCKET, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LaunchLabenvComponentListView extends VerticalLayout implements AfterNavigationObserver, ReactiveListUpdates {

    public static final String ROUTE = "launch";

    private final DirectoryController directoryController;
    private final LaunchControlGrid grid;
    private final UpdateFrontendController updateFrontendController;

    public LaunchLabenvComponentListView(DirectoryController directoryController, LaunchControlGrid grid, UpdateFrontendController updateFrontendController) {
        this.directoryController = directoryController;
        this.grid = grid;
        this.updateFrontendController = updateFrontendController;

        this.updateFrontendController.getListOfViewsWithLiveUpdates().add(this);

        final VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.add(grid);

        final VerticalLayout main = new VerticalLayout();
        main.add(gridLayout);

        add(main);
    }

    @Override
    public void updateList() {
        // if user has not visited this view then getUI() will be null
        // that's why we .map instead of using .get, map won't do anything if null is passed
        getUI().map(ui -> ui.access(() -> grid.setItems(directoryController.getLabEnvironments().values())));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        updateFrontendController.getListOfViewsWithLiveUpdates().remove(this);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(directoryController.getLabEnvironments().values());
    }

    // accessing the UI here isn't possible, because it is not yet available !
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        updateAppStateNotification();
//    }
}
