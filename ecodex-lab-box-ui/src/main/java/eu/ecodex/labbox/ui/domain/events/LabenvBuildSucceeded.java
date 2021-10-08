package eu.ecodex.labbox.ui.domain.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class LabenvBuildSucceeded extends ApplicationEvent {
    private final Path fullPathToLabenv;

    public LabenvBuildSucceeded(Object source, Path fullPathToLabenv) {
        super(source);
        this.fullPathToLabenv = fullPathToLabenv;
    }
}
