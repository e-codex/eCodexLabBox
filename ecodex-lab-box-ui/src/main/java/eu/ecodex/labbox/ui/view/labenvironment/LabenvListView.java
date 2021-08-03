package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.domain.Labenv;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = LabenvListView.ROUTE, layout = LabenvLayout.class)
@Order(1)
@TabMetadata(title = "Your Labs", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvListView extends VerticalLayout implements AfterNavigationObserver
{
    public static final String ROUTE = "labs";

    private final LabenvDetailsView details;

    private final DirectoryController directoryController;

    private LabenvGrid grid;

    public LabenvListView(DirectoryController directoryController, LabenvDetailsView details) {
        this.directoryController = directoryController;
        this.details = details;

        grid = new LabenvGrid(details);

        Button scanForLabs = new Button();
        scanForLabs.setText("Reload from Disk");
        scanForLabs.addClickListener(e -> {
            // TODO BUG when pressing reload from disk for the first time
            // then the grid contains the wrong paths
            // after pressing again, it works again
            this.directoryController.scanForLabDirectories();
            this.grid.setItems(this.directoryController.getLabenvironments());
        });

        VerticalLayout main = new VerticalLayout(grid);
        main.setAlignItems(FlexComponent.Alignment.STRETCH);
        main.add(scanForLabs);

        add(main);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        directoryController.scanForLabDirectories();
        grid.setItems(directoryController.getLabenvironments());
    }
}
