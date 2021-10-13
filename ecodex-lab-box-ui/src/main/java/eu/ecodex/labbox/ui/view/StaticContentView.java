package eu.ecodex.labbox.ui.view;

import java.util.Date;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class StaticContentView extends VerticalLayout {

	public StaticContentView(String staticContentSrc) {
		String srcName = staticContentSrc + "?param="+System.currentTimeMillis();
		IFrame staticContentFrame = new IFrame(srcName);
		staticContentFrame.setHeight("1000px");
		staticContentFrame.setSizeFull();
		staticContentFrame.getStyle().set("margin", "0");
		staticContentFrame.getStyle().set("padding", "0");
		staticContentFrame.getStyle().set("border", "none");

		add(staticContentFrame);
		
		setSizeFull();

//		setHeight("1000px");
	}

	
}
