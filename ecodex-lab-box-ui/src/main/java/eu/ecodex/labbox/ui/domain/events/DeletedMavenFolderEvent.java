package eu.ecodex.labbox.ui.domain.events;

import org.springframework.context.ApplicationEvent;

public class DeletedMavenFolderEvent extends ApplicationEvent {
    public DeletedMavenFolderEvent(Object source) {
        super(source);
    }
}
