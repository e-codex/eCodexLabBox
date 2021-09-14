package eu.ecodex.labbox.ui.domain.events;

import org.springframework.context.ApplicationEvent;

public class DeletedLabFolderEvent extends ApplicationEvent {
    public DeletedLabFolderEvent(Object source) {
        super(source);
    }
}
