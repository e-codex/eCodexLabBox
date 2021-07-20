package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.AppStarter;
import eu.ecodex.labbox.ui.domain.Labenv;
import eu.ecodex.labbox.ui.domain.events.CreatedLabboxInFilesystemEvent;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class WatchFilesystemService {

    @Getter
    private final Set<Labenv> labenvironments;

    @Getter
    private int labCount = 0;

    @Getter
    private Path containsLabenvDiretories;

    private final ApplicationEventPublisher applicationEventPublisher;

    public WatchFilesystemService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.labenvironments = new HashSet<>();
    }

    @PostConstruct
    void init() throws URISyntaxException {
        // TODO do not hardcode this
        this.containsLabenvDiretories = new File(AppStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
    }

    // run once on startup
    public void scanForLabDirectories() throws IOException {

        labenvironments.clear();
        Set<Integer> ids = new HashSet<>();

        final List<Path> labenvPaths = Files.list(containsLabenvDiretories)
                .filter(d -> d.getFileName().toString().startsWith("labenv"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        for (int i = 0; i < labenvPaths.size(); ++i) {
            Path currentPath = labenvPaths.get(i);

            // safely access the substring after "labenv"
            String[] nameAndNumberOfDirectory = currentPath.getFileName().toString().split("labenv");
            String number = null;
            if (nameAndNumberOfDirectory.length > 0) {
                number = nameAndNumberOfDirectory[1];
            }

            int parse_number = 0;
            int count = i + 1;
            try {
                parse_number = Integer.parseInt(number);
            } catch (NumberFormatException ignored) {
            }

            if (parse_number > 0 && parse_number <= labenvPaths.size()) {
                Labenv l = Labenv.builder().id(parse_number).path(currentPath).build();
                labenvironments.add(l);
                ids.add(parse_number);
                continue;
            }

            // renames file when string after "labenv" could not be parsed
            // or the following integer is not in range between 1 and count_of_all_found_labs
            int j = 1;
            while (ids.contains(j)) {
                ++j;
            }

            while (true) {
                // add to next available slot
                Labenv l = Labenv.builder().id(j).path(currentPath.resolveSibling("labenv" + j)).build();
                labenvironments.add(l);
                ids.add(j++);

                try {
                    currentPath = Files.move(currentPath, currentPath.resolveSibling("labenv" + j));
                    break;
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void watchFilesystem(String path) {
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            // TODO Logging
            e.printStackTrace();
        }

        Path realPath = Paths.get(path);

        try {
            realPath.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            // TODO Logging
            e.printStackTrace();
        }

        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {

                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");

                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_CREATE.name())) {

                        CreatedLabboxInFilesystemEvent testEvent = new CreatedLabboxInFilesystemEvent(this, "testtesttestes");
                        applicationEventPublisher.publishEvent(testEvent);

                        Labenv l = Labenv.builder().path(realPath.resolveSibling(event.context().toString())).build();
                        labenvironments.add(l);


                    }
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_DELETE.name())) {
                        Labenv l = Labenv.builder().path(realPath.resolveSibling(event.context().toString())).build();
                        labenvironments.remove(l);
                    }
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {
                        // TODO delete this branch
                        // I think this is only used when permissions are changed or so, renaming is just a delete and create
                    }

                }
                key.reset();
            }

        } catch (InterruptedException e) {
            // TODO Logging
            e.printStackTrace();
        }
    }
}
