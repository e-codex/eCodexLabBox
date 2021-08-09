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
import eu.ecodex.labbox.ui.service.LabenvService;
import lombok.Getter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = LabenvListView.ROUTE, layout = LabenvLayout.class)
@Order(1)
@TabMetadata(title = "Your Labs", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvListView extends VerticalLayout implements AfterNavigationObserver {
    public static final String ROUTE = "labs";
    private final LabenvDetailsView details;
    private final DirectoryController directoryController;
    private final LabenvService labenvService;

    @Getter
    private final LabenvGrid grid;

    public LabenvListView(DirectoryController directoryController, LabenvDetailsView details, LabenvService labenvService) {
        this.directoryController = directoryController;
        this.details = details;
        this.labenvService = labenvService;
        directoryController.getReactiveUiComponents().put("listlabs", this);
        grid = new LabenvGrid(details);

        Button scanForLabs = new Button();
        scanForLabs.setText("Reload from Disk");
        scanForLabs.addClickListener(e -> {
            // TODO BUG when pressing reload from disk for the first time
            // then the grid contains the wrong paths
            // after pressing again, it works again
            this.directoryController.scanForLabDirectories();
            this.grid.setItems(labenvService.getLabenvironments().values());
        });

        VerticalLayout main = new VerticalLayout(grid);
        main.setAlignItems(FlexComponent.Alignment.STRETCH);
        main.add(scanForLabs);

        add(main);
    }

    // Adding a spring event listener to a vaadin view causes threading problems
//    @EventListener
//    public void handleSpringEvent(BackendDataChangeEvent event);

    public void updateList() {
        getUI().map(ui -> ui.access(() -> {
            grid.setItems(labenvService.getLabenvironments().values());
        }));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        directoryController.scanForLabDirectories();
        grid.setItems(labenvService.getLabenvironments().values());
    }

}
