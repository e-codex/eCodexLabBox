package eu.ecodex.labbox.ui.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CreatedLabFolderEvent extends ApplicationEvent {

    @Getter
    private final String nameOfNewDirectory;

    public CreatedLabFolderEvent(Object source, String nameOfNewDirectory) {
        super(source);
        this.nameOfNewDirectory = nameOfNewDirectory;
    }
}
