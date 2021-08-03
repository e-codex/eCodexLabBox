package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import eu.ecodex.labbox.ui.domain.Labenv;

import java.awt.*;
import java.io.IOException;

public class LabenvGrid extends Grid<Labenv> {

    private final LabenvDetailsView details;

    public LabenvGrid(LabenvDetailsView details) {
        super();
        this.details = details;

        this.setWidth("100%");
        this.setHeightByRows(true);

        addComponentColumn(this::createShowDetailsButton).setHeader("Details").setWidth("15%");
        addColumn(Labenv::getFolderName).setHeader("(Folder) Name").setWidth("10%");
        addColumn(Labenv::getPath).setHeader("Path").setWidth("60%");
        addComponentColumn(this::createOpenFileBrowserButton).setHeader("Open in File Manager").setWidth("15%");
    }

    private Button createShowDetailsButton(Labenv labenv) {
        Button showDetails = new Button(new Icon(VaadinIcon.SEARCH));
        showDetails.addClickListener(e -> details.show(labenv));
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
