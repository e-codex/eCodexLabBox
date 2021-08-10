package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.controller.ProcessController;
import eu.ecodex.labbox.ui.service.LabenvService;
import eu.ecodex.labbox.ui.utils.StringToPathConverter;
import lombok.Getter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = LabenvSetupView.ROUTE, layout = LabenvLayout.class)
@Order(1)
@TabMetadata(title = "Setup", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvSetupView extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE = "labs";

    private final DirectoryController directoryController;
    private final LabenvService labenvService;
    private final ProcessController processController;

    private final LabenvLaunchView details;
    @Getter private final ListLabenvsGrid grid;
    private final TextField pathToLabHomeField;
    private final Label setHomeDirStatus;

    public LabenvSetupView(DirectoryController directoryController, LabenvLaunchView details, LabenvService labenvService, ProcessController processController) {
        this.directoryController = directoryController;
        this.details = details;
        this.labenvService = labenvService;
        this.processController = processController;
        directoryController.getReactiveUiComponents().put("listlabs", this);
        this.grid = new ListLabenvsGrid(details);
        // TODO try to use this for infos
        // see here: https://vaadin.com/docs/latest/ds/components/grid/#sorting
        // Item Details
//        grid.setItemDetailsRenderer();

        this.pathToLabHomeField = new TextField("Path to Lab Home Directory:");
        pathToLabHomeField.setWidth("100%");

        setHomeDirStatus = new Label();

        Binder<DirectoryController> binder = new Binder<>();
        binder.forField(pathToLabHomeField)
                .withConverter(new StringToPathConverter())
                .withValidationStatusHandler(s -> {
                    setHomeDirStatus.setText(s.getMessage().orElse("Successfully changed home directory!"));
                    setHomeDirStatus.getStyle().set("color", s.isError() ? "red" : "green");
                    setHomeDirStatus.setVisible(true);
                })
                .bind(DirectoryController::getLabenvHomeDirectory, DirectoryController::setLabenvHomeDirectory);

        // it is not possible to call DirectoryController::setLabenvHomeDirectory without this
        binder.setBean(directoryController);

        Button scanForLabs = new Button(new Icon(VaadinIcon.REFRESH));
        scanForLabs.setText("Refresh Labs");
        scanForLabs.addClickListener(e -> {
            this.directoryController.scanForLabDirectories();
            this.grid.setItems(labenvService.getLabenvironments().values());
        });

        Button createLabenv = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
        createLabenv.setText("Add Labenvironment");
        createLabenv.addClickListener(e -> this.processController.createLabenv());

        VerticalLayout gridLayout = new VerticalLayout(this.grid);
        gridLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        gridLayout.add(createLabenv);

        final VerticalLayout rescan = new VerticalLayout();
        rescan.setAlignItems(Alignment.END);
        rescan.add(scanForLabs);

        final VerticalLayout homeDirLayout = new VerticalLayout();
        homeDirLayout.add(pathToLabHomeField, setHomeDirStatus);

        final VerticalLayout main = new VerticalLayout();
        main.add(homeDirLayout, gridLayout, rescan);

        add(main);
    }

    // Adding a spring event listener to a vaadin view causes threading problems
    // e.g. @EventListener public void handleSpringEvent(BackendDataChangeEvent event);
    // This workaround enables async background threads to update the view
    // (ui.access synchronizes). This needs the @Push Annotation on the MainLayout (other places did not work)
    public void updateList() {
        getUI().map(ui -> ui.access(() -> {
            grid.setItems(labenvService.getLabenvironments().values());
        }));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        pathToLabHomeField.setValue(directoryController.getLabenvHomeDirectory().toString());
        setHomeDirStatus.setVisible(false);
        directoryController.scanForLabDirectories();
        grid.setItems(labenvService.getLabenvironments().values());
    }

}
