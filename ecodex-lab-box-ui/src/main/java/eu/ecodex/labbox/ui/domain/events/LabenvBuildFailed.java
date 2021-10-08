package eu.ecodex.labbox.ui.domain.events;

import eu.ecodex.labbox.ui.domain.Exitcode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class LabenvBuildFailed extends ApplicationEvent {

    private final Exitcode exitcode;
    private final Path fullPath;

    public LabenvBuildFailed(Object source, Exitcode exitcode, Path fullPath) {
        super(source);
        this.exitcode = exitcode;
        this.fullPath = fullPath;
    }
}
