package eu.ecodex.labbox.ui.domain.events;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class CreatedLabboxInFilesystemEvent extends ApplicationEvent {

    private final String testMessage;

    public CreatedLabboxInFilesystemEvent(Object source, String testMessage) {
        super(source);
        this.testMessage = testMessage;
    }
}
