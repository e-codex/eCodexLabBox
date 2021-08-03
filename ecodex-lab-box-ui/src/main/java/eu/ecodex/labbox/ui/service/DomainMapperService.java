package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.Labenv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@AllArgsConstructor
public class DomainMapperService {

    final WatchDirectoryConfig directoryConfig;

    public Labenv folderToLabenv(String folderName) {
        return Labenv.builder()
                .folderName(folderName)
                .path(directoryConfig.getLabenvHomeDirectory().resolve(folderName)).build();
    }

    public Labenv pathToLabenv(Path path) {
        return Labenv.builder()
                .folderName(path.getFileName().toString())
                .path(path).build();
    }
}
