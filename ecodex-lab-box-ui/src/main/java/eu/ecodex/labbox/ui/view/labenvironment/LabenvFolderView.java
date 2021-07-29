package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.service.WatchFilesystemService;
import eu.ecodex.labbox.ui.utils.StringToPathConverter;

@org.springframework.stereotype.Component
@UIScope
@Route(value = LabenvFolderView.ROUTE, layout = LabenvLayout.class)
@TabMetadata(title = "Lab Root Folder", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvFolderView extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE = "rootfolder";

    private WatchFilesystemService watchFilesystemService;

    private TextField pathToLabHomeField;

    public LabenvFolderView(WatchFilesystemService watchFilesystemService) {
        this.watchFilesystemService = watchFilesystemService;
        this.pathToLabHomeField = new TextField();
        pathToLabHomeField.setWidth("100%");

        Binder<WatchFilesystemService> binder = new Binder<>();
        binder.forField(pathToLabHomeField)
//                .withValidator( path -> new File(path).exists(), "My Message")
//                .withValidationStatusHandler( status -> {
//                    pathStatus.setText(status.getMessage().orElse(""));
//                    pathToLabHomeField.setErrorMessage(status.getMessage().orElse(""));
//                    pathStatus.setVisible(status.isError());
//                })
                .withConverter(new StringToPathConverter())
                .bind(WatchFilesystemService::getLabenvHomeDirectory, WatchFilesystemService::setLabenvHomeDirectory);

        binder.setBean(watchFilesystemService);

        add(pathToLabHomeField);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        pathToLabHomeField.setValue(watchFilesystemService.getLabenvHomeDirectory().toString());
    }
}
