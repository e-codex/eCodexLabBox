package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.repository.FileAndDirectoryRepo;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class PathMapperService {

    final FileAndDirectoryRepo fileAndDirectoryRepo;

    public PathMapperService(FileAndDirectoryRepo fileAndDirectoryRepo) {
        this.fileAndDirectoryRepo = fileAndDirectoryRepo;
    }

    public Path getFullPath(String folderName) {
        return fileAndDirectoryRepo.getLabenvHomeDirectory().resolve(folderName);
    }
}
