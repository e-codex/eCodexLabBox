package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.service.WatchFilesystemService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@UIScope
@Route(value = LabenvListView.ROUTE, layout = LabenvLayout.class)
@Order(1)
@TabMetadata(title = "Your Labs", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvListView extends VerticalLayout implements AfterNavigationObserver
{
    public static final String ROUTE = "labs";

    private final LabenvDetailsView details;

    private final WatchFilesystemService fsService;

    private LabenvGrid grid;

    public LabenvListView(WatchFilesystemService fsService, LabenvDetailsView details) {
        this.fsService = fsService;
        this.details = details;

        grid = new LabenvGrid(details);

        Button scanForLabs = new Button();
        scanForLabs.setText("Reload from Disk");
        scanForLabs.addClickListener(e -> {
            try {
                fsService.scanForLabDirectories();
//                grid.setItems(fsService.getLabenvironments());
            } catch (IOException ioException) {
                // TODO show error to user
                ioException.printStackTrace();
            }
        });

        VerticalLayout main = new VerticalLayout(grid);
        main.setAlignItems(FlexComponent.Alignment.STRETCH);
        main.add(scanForLabs);

        add(main);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        try {
            fsService.scanForLabDirectories();
        } catch (IOException e) {
            // TODO notify user
            e.printStackTrace();
        }
        grid.setItems(fsService.getLabenvironments());
    }
}
