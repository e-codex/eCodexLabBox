package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.service.NotificationService;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = HomeView.ROUTE, layout = MainLayout.class)
public class HomeView extends BaseViewVertical implements NotificationReceiver, AfterNavigationObserver {
    public static final String ROUTE = "";


   
    public HomeView(NotificationService notificationService) {
        super(notificationService);
        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("95%");
        layout.setHeightFull();
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        
        Label welcomeLabel = new Label("Welcome to e-CODEX Lab-Box");
        welcomeLabel.getStyle().set("font-size", "30px");
        welcomeLabel.getStyle().set("font-style", "bold");
        
        layout.add(welcomeLabel);
        
        Label welcomeText = new Label();
        welcomeText.setText("This is the user interface for the ecodex-lab-box. The ecodex-lab-box intends to be a starting point for those who want to get technically involved with e-CODEX building blocks. \n"
        		+ "The ecodex-labbox shows what technical components need to be installed and configured when setting up an e-CODEX environment.\n"
        		+ "It also gives insight on the message transfer and lets you exchange messages between lab environments.");
        
        layout.add(welcomeText);
        
        Label disclaimer = new Label();
        disclaimer.setText("Beware that the ecodex-lab-box is only a showcase and strictly not to be used for setting up productive environments!");
        disclaimer.getStyle().set("font-size", "20px");
        disclaimer.getStyle().set("font-style", "bold");
        disclaimer.getStyle().set("color", "red");
       
        layout.add(disclaimer);
        
        Label gettingStarted = new Label();
        gettingStarted.setText("To get started please visit the tutorial.");
        
        layout.add(gettingStarted);
      
        add(layout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateAppStateNotification();
    }
    // accessing the UI here isn't possible, because it is not yet available !
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        updateAppStateNotification();
//    }
}
