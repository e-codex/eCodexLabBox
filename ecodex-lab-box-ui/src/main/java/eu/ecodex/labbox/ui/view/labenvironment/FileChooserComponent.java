//package eu.ecodex.labbox.ui.view.labenvironment;
//
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.Tag;
//import com.vaadin.flow.component.dependency.JsModule;
//import com.vaadin.flow.component.dependency.NpmPackage;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.html.Input;
//import com.vaadin.flow.component.littemplate.LitTemplate;
//import com.vaadin.flow.component.template.Id;
//
//@Tag("native-file-chooser")
//@NpmPackage(value = "@axa-ch/input-text", version = "4.3.11")
//@JsModule("./src/NativeFileChooser.ts")
//public class FileChooserComponent extends LitTemplate {
//
//    @Id("content")
//    private Div content;
//
//    @Id("files")
//    private Input foo;
//
//    public FileChooserComponent() {
//        foo.addValueChangeListener(v -> {
//                System.out.println("" + v.getValue());
//        });
//    }
//
//    public void setContent(Component content) {
//        this.content.removeAll();
//        this.content.add(content);
//    }
//}
