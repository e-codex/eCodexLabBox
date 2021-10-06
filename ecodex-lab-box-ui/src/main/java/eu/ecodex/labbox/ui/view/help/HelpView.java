package eu.ecodex.labbox.ui.view.help;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import eu.ecodex.labbox.ui.view.BaseViewVertical;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = HelpView.ROUTE, layout = HelpLayout.class)
@Order(1)
@TabMetadata(title = "Help1", icon = VaadinIcon.LIGHTBULB, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = HelpLayout.TAB_GROUP_NAME)
public class HelpView extends BaseViewVertical implements AfterNavigationObserver, NotificationReceiver {

    public static final String ROUTE = "help1";

    public HelpView(UpdateFrontendController updateFrontendController) {
        super(updateFrontendController);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateAppStateNotification();
    }
}
