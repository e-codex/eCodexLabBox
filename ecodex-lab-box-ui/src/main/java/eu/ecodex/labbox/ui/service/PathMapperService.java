package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class PathMapperService {

    final WatchDirectoryConfig directoryConfig;

    public PathMapperService(WatchDirectoryConfig directoryConfig) {
        this.directoryConfig = directoryConfig;
    }

    public Path getFullPath(String folderName) {
        return directoryConfig.getLabenvHomeDirectory().resolve(folderName);
    }
}
