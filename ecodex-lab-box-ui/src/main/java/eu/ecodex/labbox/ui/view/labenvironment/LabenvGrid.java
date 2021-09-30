package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.SortDirection;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.entities.LabenvComparator;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;

public class LabenvGrid extends Grid<Labenv> {

    public LabenvGrid(LaunchLabenvComponentListView details) {
        super();

        this.setWidth("100%");
        this.setHeightByRows(true);

        addColumn(l -> l.getPath().getFileName().toString()).setHeader("Name").setWidth("15%")
                .setComparator(new LabenvComparator());
        addColumn(Labenv::getPath).setHeader("Path").setWidth("65%");
        addComponentColumn(this::createOpenFileBrowserButton).setHeader("Open in File Manager").setWidth("20%");

        ArrayList<GridSortOrder<Labenv>> sort = new ArrayList<>();
        GridSortOrder<Labenv> order = new GridSortOrder<>(this.getColumns().get(1), SortDirection.ASCENDING);
        sort.add(order);
        this.sort(sort);
    }

    private Button createOpenFileBrowserButton(Labenv labenv) {
        Button openFolder = new Button(new Icon(VaadinIcon.FOLDER_OPEN));
        openFolder.addClickListener(e -> {
            try {
                Desktop.getDesktop().open(labenv.getPath().toFile());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        return openFolder;
    }
}
