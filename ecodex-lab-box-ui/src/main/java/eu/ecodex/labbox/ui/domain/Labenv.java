package eu.ecodex.labbox.ui.domain;



import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@Builder
@EqualsAndHashCode
public class Labenv {
    private final int id;
    private final Path path;
}
