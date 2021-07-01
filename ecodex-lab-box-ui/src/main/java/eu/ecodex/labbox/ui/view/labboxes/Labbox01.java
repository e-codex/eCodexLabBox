package eu.ecodex.labbox.ui.view.labboxes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.AppStarter;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
@Route(value = Labbox01.ROUTE, layout = LabboxLayout.class)
@UIScope
@TabMetadata(title = "Labbox 01", tabGroup = LabboxLayout.TAB_GROUP_NAME)
public class Labbox01 extends VerticalLayout {

    public static final String ROUTE = "labbox01";

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
}
