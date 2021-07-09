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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
@UIScope
@Route(value = LabenvDetails.ROUTE, layout = LabenvLayout.class)
@TabMetadata(title = "Lab Details", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class LabenvDetails extends VerticalLayout implements HasUrlParameter<Integer> {

    public static final String ROUTE = "labdetails";

    @PostConstruct
    void init() {
        Button button = new Button(new Icon(VaadinIcon.BOMB));
        button.setText("Open Directory");
        button.addClickListener(c -> {
            try {
                openLabboxDirectory();
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
        add(button);
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
        UI.getCurrent().navigate(LabenvDetails.class, lab.getId());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Integer parameter) {
        if(parameter!=null) {
            // TODO load labenv
        }else {
            // TODO show maybe a CREATE View ???
        }
    }
}
