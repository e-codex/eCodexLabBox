package eu.ecodex.labbox.ui.configuration;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@UIScope
@SpringComponent
public @interface TabMetadata {
    String title();
    VaadinIcon icon();
    TabVariant themeVariant();
    String tabGroup();
}
