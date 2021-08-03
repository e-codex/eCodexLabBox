package eu.ecodex.labbox.ui.domain;



import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.file.Path;

@Builder
@EqualsAndHashCode
public class Labenv {

    @Getter
    private final String folderName;

    @Getter
    private final Path path;

}
