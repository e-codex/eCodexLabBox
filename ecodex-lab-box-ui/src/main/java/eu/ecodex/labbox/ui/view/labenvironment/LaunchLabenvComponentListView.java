package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.service.LabenvService;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.view.BaseViewVertical;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = LaunchLabenvComponentListView.ROUTE, layout = LabenvLayout.class)
@Order(2)
@TabMetadata(title = "Launch", icon = VaadinIcon.ROCKET, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LaunchLabenvComponentListView extends BaseViewVertical implements AfterNavigationObserver, ReactiveListUpdates, NotificationReceiver {

    public static final String ROUTE = "launch";

    private final LabenvService labenvService;
    private final LaunchControlGrid grid;

    public LaunchLabenvComponentListView(DirectoryController directoryController, LabenvService labenvService, LaunchControlGrid grid, NotificationService notificationService)
    {
        super(notificationService);
        this.labenvService = labenvService;
        this.grid = grid;
        directoryController.getReactiveLists().put("launchlist", this);
        directoryController.getBroadcastReceivers().add(this);

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
        getUI().map(ui -> ui.access(() -> grid.setItems(labenvService.getLabenvironments().values())));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(labenvService.getLabenvironments().values());
        updateAppStateNotification();
    }

    // accessing the UI here isn't possible, because it is not yet available !
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        updateAppStateNotification();
//    }
}
