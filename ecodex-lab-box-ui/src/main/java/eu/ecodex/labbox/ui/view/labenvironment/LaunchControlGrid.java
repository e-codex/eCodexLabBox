package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.controller.ProcessController;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.domain.entities.LabenvComparator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@UIScope
@Component
public class LaunchControlGrid extends Grid<Labenv> {

    private final ProcessController processController;

    private Notification notFoundOrStillLoading;
    private Notification noExecutable;

    public LaunchControlGrid(ProcessController processController) {
        this.processController = processController;

        notFoundOrStillLoading = new Notification("What port? There is no configuration file. Maybe the lab is still building?");
        notFoundOrStillLoading.setDuration(3000);
        notFoundOrStillLoading.setPosition(Notification.Position.MIDDLE);
        notFoundOrStillLoading.addThemeVariants(NotificationVariant.LUMO_ERROR);

        noExecutable = new Notification("No executable found! Maybe the lab is still building?");
        noExecutable.setDuration(3000);
        noExecutable.setPosition(Notification.Position.MIDDLE);
        noExecutable.addThemeVariants(NotificationVariant.LUMO_ERROR);

        this.setWidth("100%");
        this.setHeightByRows(true);

        addColumn(l -> l.getPath().getFileName().toString()).setHeader("Name").setWidth("10%")
                .setComparator(new LabenvComparator()).setId("Name");
//        addColumn("Status: Started, Stopped, Etc.").setHeader("Status").setWidth("30%");
        addComponentColumn(this::createGatewaySection).setHeader("Gateway").setWidth("20%");
        addComponentColumn(this::createConnectorSection).setHeader("Connector").setWidth("20%");
        addComponentColumn(this::createClientSection).setHeader("Client").setWidth("20%");
        addComponentColumn(this::createClientMessageSection).setHeader("Message").setWidth("20%");

        ArrayList<GridSortOrder<Labenv>> sort = new ArrayList<>();
        GridSortOrder<Labenv> order = new GridSortOrder<>(this.getColumns().get(0), SortDirection.ASCENDING);
        sort.add(order);
        this.sort(sort);
    }

    private VerticalLayout createGatewaySection(Labenv labenv) {
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(createLaunchGatewayButton(labenv), createStopGatewayButton(labenv), createOpenGatewayUIButton(labenv));
        return verticalLayout;
    }

    private VerticalLayout createConnectorSection(Labenv labenv) {
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(createLaunchConnectorButton(labenv), createStopConnectorButton(labenv), createOpenConnectorUIButton(labenv));
        return verticalLayout;
    }


    private VerticalLayout createClientSection(Labenv labenv) {
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(createLaunchClientButton(labenv), createStopClientButton(labenv), createOpenClientUIButton(labenv));
        return verticalLayout;
    }
    
    private VerticalLayout createClientMessageSection(Labenv labenv) {
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(createOpenClientMessageButton(labenv));
        return verticalLayout;
    }

    private Button createLaunchGatewayButton(Labenv labenv) {
        Button launchGateway = new Button(new Icon(VaadinIcon.PLAY));
        launchGateway.setText("Start Gateway");
        launchGateway.addClickListener(c -> {
            try {
                this.processController.startGateway(labenv);
            } catch (IOException e) {
                noExecutable.open();
                e.printStackTrace();
            }
        });
        return launchGateway;
    }

    private Button createStopGatewayButton(Labenv labenv) {
        Button stopGateway = new Button(new Icon(VaadinIcon.STOP));
        stopGateway.setText("Stop Gateway");
        stopGateway.addClickListener(c -> {
            try {
                this.processController.stopGateway(labenv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return stopGateway;
    }

    private Button createOpenGatewayUIButton(Labenv labenv) {
        Button openGatewayUI = new Button(new Icon(VaadinIcon.DASHBOARD));
        openGatewayUI.setText("Open Gateway UI");
        openGatewayUI.addClickListener(c -> {
            if (labenv.getGatewayPort() == null) {
                notFoundOrStillLoading.open();
            } else {
                UI.getCurrent().getPage().open("http://localhost:" + labenv.getGatewayPort() + "/domibus");
            }
        });
        return openGatewayUI;
    }

    private Button createLaunchConnectorButton(Labenv labenv) {
        Button launchConnector = new Button(new Icon(VaadinIcon.PLAY));
        launchConnector.setText("Start Connector");
        launchConnector.addClickListener(c -> {
            try {
                this.processController.startConnector(labenv);
            } catch (IOException e) {
                noExecutable.open();
                e.printStackTrace();
            }
        });
        return launchConnector;
    }

    private Button createStopConnectorButton(Labenv labenv) {
        Button stopConnector = new Button(new Icon(VaadinIcon.STOP));
        stopConnector.setText("Stop Connector");
        stopConnector.addClickListener(c -> this.processController.stopConnector(labenv));
        return stopConnector;
    }

    private Button createOpenConnectorUIButton(Labenv labenv) {
        Button openConnectorUI = new Button(new Icon(VaadinIcon.DASHBOARD));
        openConnectorUI.setText("Open Connector UI");
        openConnectorUI.addClickListener(c -> {
            if (labenv.getConnectorPort() == null) {
                notFoundOrStillLoading.open();
            } else {
                UI.getCurrent().getPage().open("http://localhost:" + labenv.getConnectorPort());
            }
        });
        return openConnectorUI;
    }

    private Button createLaunchClientButton(Labenv labenv) {

        Button launchClient = new Button(new Icon(VaadinIcon.PLAY));
        launchClient.setText("Start Client");
        launchClient.addClickListener(c -> {
            try {
                this.processController.startClient(labenv);
            } catch (IOException e) {
                noExecutable.open();
                e.printStackTrace();
            }
        });
        return launchClient;
    }

    private Button createStopClientButton(Labenv labenv) {
        Button stopClient = new Button(new Icon(VaadinIcon.STOP));
        stopClient.setText("Stop Connector Client");
        stopClient.addClickListener(c -> this.processController.stopClient(labenv));
        return stopClient;
    }

    private Button createOpenClientUIButton(Labenv labenv) {
        Button openClientUI = new Button(new Icon(VaadinIcon.DASHBOARD));
        openClientUI.setText("Open Connector Client UI");
        openClientUI.addClickListener(c -> {
            if (labenv.getClientPort() == null) {
                notFoundOrStillLoading.open();
            } else {
                UI.getCurrent().getPage().open("http://localhost:" + labenv.getClientPort());
            }
        });
        return openClientUI;
    }
    
    private Button createOpenClientMessageButton(Labenv labenv) {
        Button openClientUI = new Button(new Icon(VaadinIcon.ENVELOPE));
        openClientUI.setText("Trigger Connector Client Message");
        openClientUI.addClickListener(c -> {
            if (labenv.getClientPort() == null) {
                notFoundOrStillLoading.open();
            } else {
                UI.getCurrent().getPage().open("http://localhost:" + labenv.getClientPort() + "/messages/sendMessage/100");
            }
        });
        return openClientUI;
    }

}
