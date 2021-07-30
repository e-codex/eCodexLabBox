package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.AppStarter;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.domain.Labenv;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
@UIScope
@Route(value = LabenvDetailsView.ROUTE, layout = LabenvLayout.class)
@Order(2)
@TabMetadata(title = "Lab Details", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvDetailsView extends VerticalLayout implements HasUrlParameter<Integer>, AfterNavigationObserver {

    public static final String ROUTE = "labdetails";

    @PostConstruct
    void init() {
        Button launchAll = new Button(new Icon(VaadinIcon.BOMB));
        launchAll.setText("Start Lab");
        launchAll.addClickListener(c -> {
            try {
                startLab();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button openDir = new Button(new Icon(VaadinIcon.FOLDER_OPEN));
        openDir.setText("Open Directory");
        openDir.addClickListener(c -> {
            try {
                openLabboxDirectory();
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
        add(openDir);
    }

    void startLab() throws IOException {
        if (SystemUtils.IS_OS_WINDOWS) {
            // TODO maybe better to use ProcessBuilder
            Runtime.getRuntime().exec("explorer.exe /select, ");
        } else if (SystemUtils.IS_OS_LINUX) {

        } else if (SystemUtils.IS_OS_MAC) {

        }
    }

    void openLabboxDirectory() throws URISyntaxException, IOException {
        File file = new File(AppStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI());

//        Debug
//        System.out.println(file.getAbsolutePath());
//        System.out.println(Desktop.isDesktopSupported()); // true

//        Platform independent
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);

//        Platform dependent
//        Runtime.getRuntime().exec("explorer.exe /select, " + workingDirectory);
    }

    public void show(Labenv lab) {
        UI.getCurrent().navigate(LabenvDetailsView.class, lab.getId());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Integer parameter) {
        if(parameter!=null) {
            // TODO load labenv
        }else {
            // TODO show maybe a CREATE View ???
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }
}
