package eu.ecodex.labbox.ui.view.settings;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.ecodex.labbox.ui.configuration.TabMetadata;
import eu.ecodex.labbox.ui.controller.SettingsController;
import eu.ecodex.labbox.ui.controller.UpdateFrontendController;
import eu.ecodex.labbox.ui.domain.Proxy;
import eu.ecodex.labbox.ui.utils.IpAddressValidator;
import eu.ecodex.labbox.ui.utils.PortValidator;
import eu.ecodex.labbox.ui.view.BaseViewVertical;
import eu.ecodex.labbox.ui.view.labenvironment.NotificationReceiver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@UIScope
@Route(value = ProxySettingsView.ROUTE, layout = SettingsLayout.class)
@Order(1)
@TabMetadata(title = "Proxy", icon = VaadinIcon.SERVER, themeVariant = TabVariant.LUMO_ICON_ON_TOP, tabGroup = SettingsLayout.TAB_GROUP_NAME)
public class ProxySettingsView extends BaseViewVertical implements AfterNavigationObserver, NotificationReceiver {

    public static final String ROUTE = "proxy";

    private final SettingsController settingsController;

    private final TextField proxyHost;
    private final TextField proxyPort;

    final Label infoLabel;

    private Proxy proxyBeingEdited;

    private final Binder<Proxy> proxyBinder;

    public ProxySettingsView(UpdateFrontendController updateFrontendController, SettingsController settingsController) {
        super(updateFrontendController);
        this.settingsController = settingsController;

        proxyHost = new TextField();
        proxyHost.setValueChangeMode(ValueChangeMode.EAGER);

        proxyPort = new TextField();
        proxyPort.setValueChangeMode(ValueChangeMode.EAGER);

        proxyBinder = new Binder<>();
        proxyBinder.forField(proxyHost)
                .asRequired("You must specify an ip address!")
                .withConverter(new IpAddressValidator())
                .bind(Proxy::getIp, Proxy::setIp);

        proxyBinder.forField(proxyPort)
                .asRequired("You must specify a proxy port!")
                .withConverter(new PortValidator())
                .bind(Proxy::getPort, Proxy::setPort);

        infoLabel = new Label();
        infoLabel.setVisible(false);

        final FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("500px");

        formLayout.addFormItem(proxyHost, "Proxy IP Address");
        formLayout.addFormItem(proxyPort, "Proxy Port");

        final HorizontalLayout validationMessages = new HorizontalLayout();
        validationMessages.add(infoLabel);

        add(formLayout, validationMessages);
    }

    private void valueChange(HasValue.ValueChangeEvent<String> e) {
        if (proxyBinder.writeBeanIfValid(proxyBeingEdited)) {
            infoLabel.setText("Saved bean values: " + proxyBeingEdited);
            infoLabel.getStyle().set("color", "green");
            infoLabel.setVisible(true);
            settingsController.setProxy(proxyBeingEdited);
        } else {
            BinderValidationStatus<Proxy> validate = proxyBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            infoLabel.setText("There are errors: " + errorText);
            infoLabel.getStyle().set("color", "red");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        proxyBeingEdited = settingsController.getProxy();
        proxyBinder.setBean(this.proxyBeingEdited);

        proxyHost.addValueChangeListener(this::valueChange);
        proxyPort.addValueChangeListener(this::valueChange);

        updateAppStateNotification();
    }
}
