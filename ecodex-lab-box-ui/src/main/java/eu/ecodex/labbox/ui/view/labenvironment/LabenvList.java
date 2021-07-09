package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.service.WatchDirectoryService;
import eu.ecodex.labbox.ui.service.WatchFilesystemService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@UIScope
@Route(value = LabenvList.ROUTE, layout = LabenvLayout.class)
@TabMetadata(title = "Your Labs", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvList extends VerticalLayout
{
    public static final String ROUTE = "labs";

    private final LabenvDetails details;

    private final WatchFilesystemService fsService;
    private final WatchDirectoryService watcher;

    private LabenvGrid grid;

    public LabenvList(WatchFilesystemService fsService, WatchDirectoryService watcher, LabenvDetails details) {
        this.fsService = fsService;
        this.watcher = watcher;
        this.details = details;
    }

    @PostConstruct
    void init() {
        try {
            fsService.scanForLabDirectories();
        } catch (IOException e) {
            e.printStackTrace();
        }
        grid = new LabenvGrid(details);
        grid.setItems(fsService.getLabenvironments());

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
}
