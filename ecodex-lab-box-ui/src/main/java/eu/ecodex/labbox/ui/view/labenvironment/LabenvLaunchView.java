package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.service.LabenvService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@UIScope
@Route(value = LabenvLaunchView.ROUTE, layout = LabenvLayout.class)
@Order(2)
@TabMetadata(title = "Launch", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvLaunchView extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE = "launch";

    private final LabenvService labenvService;
    private final ListWithLaunchControlGrid grid;

    public LabenvLaunchView(LabenvService labenvService, ListWithLaunchControlGrid grid)
    {
        this.labenvService = labenvService;
        this.grid = grid;

        final VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.add(grid);

        final VerticalLayout main = new VerticalLayout();
        main.add(gridLayout);

        add(main);
    }

    public void updateList() {
        getUI().map(ui -> ui.access(() -> {
            grid.setItems(labenvService.getLabenvironments().values());
        }));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(labenvService.getLabenvironments().values());
    }
}
