package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.DirectoryController;
import eu.ecodex.labbox.ui.controller.ProcessController;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.service.PathMapperService;
import eu.ecodex.labbox.ui.service.PlatformService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
@UIScope
@Route(value = LabenvDetailsView.ROUTE, layout = LabenvLayout.class)
@Order(2)
@TabMetadata(title = "Lab Details", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvDetailsView extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    public static final String ROUTE = "labdetails";

    final PlatformService platformService;

    // TODO remove
    final DirectoryController directoryController;

    final PathMapperService pathMapperService;
    final ProcessController processController;
    Labenv labenv;
    TextField labName;

    public LabenvDetailsView(PlatformService platformService, DirectoryController directoryController,
                             PathMapperService pathMapperService, ProcessController processController)
    {
        this.platformService = platformService;
        this.directoryController = directoryController;
        this.pathMapperService = pathMapperService;
        this.processController = processController;

        // Labenv
        this.labName = new TextField();
        this.labName.setReadOnly(true);

        HorizontalLayout labenvSection = new HorizontalLayout();

        Button openDir = new Button(new Icon(VaadinIcon.FOLDER_OPEN));
        openDir.setText("Open Directory");
        openDir.addClickListener(c -> {
            try {
                openLabboxDirectory();
            } catch (URISyntaxException | IOException e) {
                // TODO show errors to user
                e.printStackTrace();
            }
        });

        labenvSection.add(this.labName, openDir);

        // Gateway
        HorizontalLayout gatewaySection = new HorizontalLayout();

        TextField gatewaySectionTitle = new TextField();
        gatewaySectionTitle.setValue("Gateway");
        gatewaySectionTitle.setReadOnly(true);

        Button launchGateway = new Button(new Icon(VaadinIcon.BOMB));
        launchGateway.setText("Start Gateway");
        launchGateway.addClickListener(c -> {
            try {
                this.processController.startGateway(labenv);
            } catch (IOException e) {
                // TODO show errors to user
                e.printStackTrace();
            }
        });

        Button stopGateway = new Button(new Icon(VaadinIcon.STOP));
        stopGateway.setText("Stop Gateway");
        stopGateway.addClickListener(c -> {
            try {
                this.processController.stopGateway(labenv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button openGatewayUI = new Button(new Icon(VaadinIcon.DASHBOARD));
        openGatewayUI.setText("Open Gateway UI");
        openGatewayUI.addClickListener(c -> {
            UI.getCurrent().getPage().open("http://localhost:"+labenv.getGatewayPort());
        });

        gatewaySection.add(gatewaySectionTitle, launchGateway, stopGateway, openGatewayUI);

        // Connector
        HorizontalLayout connectorSection = new HorizontalLayout();

        TextField connectorSectionTitle = new TextField();
        connectorSectionTitle.setValue("Connector");
        connectorSectionTitle.setReadOnly(true);

        Button launchConnector = new Button(new Icon(VaadinIcon.ROCKET));
        launchConnector.setText("Start Connector");
        launchConnector.addClickListener(c -> {
            try {
                this.processController.startConnector(labenv);
            } catch (IOException e) {
                // TODO notify user
                e.printStackTrace();
            }
        });


        Button stopConnector = new Button(new Icon(VaadinIcon.STOP));
        stopConnector.setText("Stop Connector");
        stopConnector.addClickListener(c -> {
            this.processController.stopConnector(labenv);
        });

        Button openConnectorUI = new Button(new Icon(VaadinIcon.DASHBOARD));
        openConnectorUI.setText("Open Connector UI");
        openConnectorUI.addClickListener(c -> {
            UI.getCurrent().getPage().open("http://localhost:"+labenv.getConnectorPort());
        });

        connectorSection.add(connectorSectionTitle, launchConnector, stopConnector, openConnectorUI);

        // Connector Client
        HorizontalLayout clientSection = new HorizontalLayout();

        TextField clientSectionTitle = new TextField();
        clientSectionTitle.setValue("Client");
        clientSectionTitle.setReadOnly(true);

        Button launchClient = new Button(new Icon(VaadinIcon.STAR));
        launchClient.setText("Start Client");
        launchClient.addClickListener(c -> {
            try {
                this.processController.startClient(labenv);
            } catch (IOException e) {
                // TODO show errors to user
                e.printStackTrace();
            }
        });

        Button stopClient = new Button(new Icon(VaadinIcon.STOP));
        stopClient.setText("Stop Connector Client");
        stopClient.addClickListener(c -> {
            // TODO implement this
        });

        Button openClientUI = new Button(new Icon(VaadinIcon.DASHBOARD));
        openClientUI.setText("Open Connector Client UI");
        openClientUI.addClickListener(c -> {
            UI.getCurrent().getPage().open("http://localhost:"+labenv.getClientPort());
        });
        clientSection.add(clientSectionTitle, launchClient, stopClient, openClientUI);

        add(labenvSection, gatewaySection, connectorSection, clientSection);
    }

    void openLabboxDirectory() throws URISyntaxException, IOException {
//        Platform independent
        Desktop desktop = Desktop.getDesktop();
        desktop.open(labenv.getPath().toFile());
    }

    public void show(Labenv lab) {
        UI.getCurrent().navigate(LabenvDetailsView.class, lab.getPath().getFileName().toString());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String folderName) {
        if (folderName != null
            && directoryController.getLabenvironments().containsKey(pathMapperService.getFullPath(folderName)))
        {
            this.labenv = directoryController.getLabenvironments()
                    .get(pathMapperService.getFullPath(folderName));
        } else {
            // TODO show maybe a CREATE View instead of redirecting to list???

            // Todo show info
            Notification.show("Labbox not found", 5_000, Notification.Position.TOP_CENTER);
            beforeEvent.forwardTo(LabenvListView.class);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (labenv != null) {
            labName.setValue(labenv.getPath().getFileName().toString());
        } else {
            labName.setValue("");
        }
    }
}
