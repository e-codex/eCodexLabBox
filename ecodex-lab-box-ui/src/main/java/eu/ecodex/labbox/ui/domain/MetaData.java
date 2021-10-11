package eu.ecodex.labbox.ui.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@EqualsAndHashCode
public class MetaData {

    private Path path;
    private Exitcode exitcode;
}
