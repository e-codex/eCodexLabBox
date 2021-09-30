package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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


    Label l = new Label();

    public HomeView(NotificationService notificationService) {
        super(notificationService);
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("95%");
        horizontalLayout.setHeightFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        horizontalLayout.add(l);

        l.setText("Welcome to Lab-box UI");
        l.getStyle().set("font-size", "large");

        add(horizontalLayout);
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
