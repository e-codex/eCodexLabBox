package eu.ecodex.labbox.ui.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeletedLabenvFolderEvent extends ApplicationEvent {

    @Getter
    private final String nameOfDeletedDirectory;

    public DeletedLabenvFolderEvent(Object source, String nameOfDeletedDirectory) {
        super(source);
        this.nameOfDeletedDirectory = nameOfDeletedDirectory;
    }
}
