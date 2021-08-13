package eu.ecodex.labbox.ui.repository;

import eu.ecodex.labbox.ui.AppStarter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Repository
public class FileAndDirectoryRepo {

    @Getter
    @Setter
    private Optional<Path> mavenExecutable;

    @Getter
    private Path labenvHomeDirectory;

    public FileAndDirectoryRepo(@Value("${labenvironments.homedir}") String path)
    {
        this.mavenExecutable = Optional.empty();

        // if the app can't set the labhome based on the property string it
        // sets the working directory to wherever the app was started from
        if (!(path != null && setLabenvHomeDirectory(path)))
        {
            try {
                this.labenvHomeDirectory = new File(AppStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
            } catch (URISyntaxException e) {
                log.debug("unable to set default monitoring folder");
            }
        }
    }

    public synchronized boolean setLabenvHomeDirectory(String path) {
        File file = new File(path);
        if (file.exists()) {
            this.labenvHomeDirectory = file.toPath();
            return true;
        }
        return false;
    }

    public synchronized void setLabenvHomeDirectory(Path path) {
        this.labenvHomeDirectory = path;
    }
}
