package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
import eu.ecodex.labbox.ui.domain.AppState;
import eu.ecodex.labbox.ui.service.LabenvService;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.utils.StringToPathConverter;
import eu.ecodex.labbox.ui.view.BaseViewVertical;
import lombok.Getter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@UIScope
@Route(value = LabenvSetupListView.ROUTE, layout = LabenvLayout.class)
@Order(1)
@TabMetadata(title = "Setup", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvSetupListView extends BaseViewVertical implements AfterNavigationObserver, ReactiveListUpdates, BroadcastReceiver {

    public static final String ROUTE = "labs";

    private final DirectoryController directoryController;
    private final LabenvService labenvService;
    private final ProcessController processController;

    private final LaunchLabenvComponentListView details;
    @Getter
    private final LabenvGrid grid;
    private final TextField pathToLabHomeField;
    private final Label setHomeDirStatus;

    public LabenvSetupListView(NotificationService notificationService, DirectoryController directoryController,
                               LaunchLabenvComponentListView details, LabenvService labenvService,
                               ProcessController processController) {
        super(notificationService);
        this.directoryController = directoryController;
        this.details = details;
        this.labenvService = labenvService;
        this.processController = processController;
        directoryController.getReactiveLists().put("setuplist", this);
        directoryController.getBroadcastReceivers().add(this);

        this.grid = new LabenvGrid(details);
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
            this.directoryController.searchForLabenvDirectories();
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
    @Override
    public void updateList() {
        // if user has not visited this view then getUI() will be null
        // that's why we .map instead of using .get, map won't do anything if null is passed
        getUI().map(ui -> ui.access(() -> {
            grid.setItems(labenvService.getLabenvironments().values());
        }));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        pathToLabHomeField.setValue(directoryController.getLabenvHomeDirectory().toString());
        setHomeDirStatus.setVisible(false);
        grid.setItems(labenvService.getLabenvironments().values());
        updateAppStateNotification();
    }

    // accessing the UI here isn't possible, because it is not yet available !
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        updateAppStateNotification();
//    }

}
