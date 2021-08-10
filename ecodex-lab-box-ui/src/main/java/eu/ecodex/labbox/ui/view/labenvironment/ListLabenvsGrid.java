package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.provider.SortDirection;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.entities.LabenvComparator;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ListLabenvsGrid extends Grid<Labenv> {

    private final LabenvLaunchView details;

    private final Notification todoDetails;

    public ListLabenvsGrid(LabenvLaunchView details) {
        super();
        this.details = details;
        this.todoDetails = new Notification("Currently dropped, because the details section is now a list!");
        this.todoDetails.setDuration(3000);
        this.todoDetails.addThemeVariants(NotificationVariant.LUMO_ERROR);
        this.todoDetails.setPosition(Notification.Position.MIDDLE);

        this.setWidth("100%");
        this.setHeightByRows(true);

        addComponentColumn(this::createShowDetailsButton).setHeader("Details").setWidth("15%");
        addColumn(l -> l.getPath().getFileName().toString()).setHeader("(Folder) Name").setWidth("10%")
                .setComparator(new LabenvComparator());
        addColumn(Labenv::getPath).setHeader("Path").setWidth("60%");
        addComponentColumn(this::createOpenFileBrowserButton).setHeader("Open in File Manager").setWidth("15%");

        ArrayList<GridSortOrder<Labenv>> sort = new ArrayList<>();
        GridSortOrder<Labenv> order = new GridSortOrder<>(this.getColumns().get(1), SortDirection.ASCENDING);
        sort.add(order);
        this.sort(sort);
    }

    private Button createShowDetailsButton(Labenv labenv) {
        Button showDetails = new Button(new Icon(VaadinIcon.SEARCH));
//        showDetails.addClickListener(e -> details.show(labenv));
        showDetails.addClickListener(e -> todoDetails.open());
        return showDetails;
    }

    private Button createOpenFileBrowserButton(Labenv labenv) {
        Button showDetails = new Button(new Icon(VaadinIcon.FOLDER_OPEN));
        showDetails.addClickListener(e -> {
            try {
                Desktop.getDesktop().open(labenv.getPath().toFile());
            } catch (IOException ioException) {
                // TODO show error to user
                ioException.printStackTrace();
            }
        });
        return showDetails;
    }
}
