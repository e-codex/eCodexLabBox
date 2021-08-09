package eu.ecodex.labbox.ui.view.labenvironment;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.ProcessController;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = CreateLabenvView.ROUTE, layout = LabenvLayout.class)
@Order(3)
@TabMetadata(title = "Create Lab", tabGroup = LabenvLayout.TAB_GROUP_NAME)
public class CreateLabenvView extends VerticalLayout {
    public static final String ROUTE = "create";

    private final ProcessController processController;

    public CreateLabenvView(ProcessController processController) {
        this.processController = processController;

        Button createNextLab = new Button(new Icon(VaadinIcon.PLAY));
        createNextLab.setText("Create Labenvironment");
        // TODO errorhandling?
        createNextLab.addClickListener(c -> this.processController.createLabenv());

        add(createNextLab);
    }
}
