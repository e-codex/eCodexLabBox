package eu.ecodex.labbox.ui.configuration;

import eu.ecodex.labbox.ui.repository.FileAndDirectoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Configuration
public class WatchDirectoryConfig {

    private final FileAndDirectoryRepo fileAndDirectoryRepo;

    public WatchDirectoryConfig(FileAndDirectoryRepo fileAndDirectoryRepo) {
        this.fileAndDirectoryRepo = fileAndDirectoryRepo;
    }

//    @Bean ???
    public WatchService watchService() {
        log.debug("MONITORING_FOLDER: {}", fileAndDirectoryRepo.getLabenvHomeDirectory());
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();

            if (!Files.isDirectory(fileAndDirectoryRepo.getLabenvHomeDirectory())) {
                throw new RuntimeException("incorrect monitoring folder: " + fileAndDirectoryRepo.getLabenvHomeDirectory());
            }

            fileAndDirectoryRepo.getLabenvHomeDirectory().register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
        } catch (IOException e) {
            log.error("exception for watch service creation:", e);
        }
        return watchService;

    }
}
