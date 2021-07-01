package eu.ecodex.labbox.ui.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Route(value = HomeView.ROUTE, layout = MainLayout.class)
@PageTitle("Labbox - UI")
public class HomeView extends VerticalLayout
{
    public static final String ROUTE = "";

    Label l = new Label();

    public HomeView() {
        l.setText("Welcome to Labbox UI");
        add(l);
    }


}
