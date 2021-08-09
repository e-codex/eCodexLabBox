package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.utils.StringToPathConverter;
import org.springframework.core.annotation.Order;

@org.springframework.stereotype.Component
@UIScope
@Route(value = LabenvHomeDirectoryView.ROUTE, layout = LabenvLayout.class)
@Order(3)
@TabMetadata(title = "Lab Home Directory", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvHomeDirectoryView extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE = "homedir";

    private DirectoryController directoryController;

    private TextField pathToLabHomeField;

    public LabenvHomeDirectoryView(DirectoryController directoryController) {
        this.directoryController = directoryController;
        this.pathToLabHomeField = new TextField();
        pathToLabHomeField.setWidth("100%");

        Binder<DirectoryController> binder = new Binder<>();
        binder.forField(pathToLabHomeField)
//                .withValidator( path -> new File(path).exists(), "My Message")
//                .withValidationStatusHandler( status -> {
//                    pathStatus.setText(status.getMessage().orElse(""));
//                    pathToLabHomeField.setErrorMessage(status.getMessage().orElse(""));
//                    pathStatus.setVisible(status.isError());
//                })
                .withConverter(new StringToPathConverter())
                .bind(DirectoryController::getLabenvHomeDirectory, DirectoryController::setLabenvHomeDirectory);

        // it is not possible to call DirectoryController::setLabenvHomeDirectory without this
        binder.setBean(directoryController);

        add(pathToLabHomeField);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        pathToLabHomeField.setValue(directoryController.getLabenvHomeDirectory().toString());
    }
}
