package eu.ecodex.labbox.ui.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Repository;

import java.io.File;
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

    public FileAndDirectoryRepo(@Value("${spring.profiles.active}") String activeProfile) {
        this.mavenExecutable = Optional.empty();

        final ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        if (activeProfile.equals("prod")) {
            // the application distribution package contains this folder
            this.labenvHomeDirectory = applicationHome.getDir().toPath().getParent().resolve("ecodex-lab-box");
        } else {
            // change ecodex-lab-box/ecodex-lab-box-ui/target/classes
            // to ecodex-lab-box
            // into ecodex-lab-box/ecodex-lab-box
            this.labenvHomeDirectory = findExecutableOnPath("mvn");

        }
    }

    private Path findExecutableOnPath(String name) {
        for (String dirname : System.getenv("PATH").split(File.pathSeparator)) {
            File file = new File(dirname, name);
            if (file.isFile() && file.canExecute()) {
                return file.toPath();
            }
        }
        throw new AssertionError("should have found the executable");
    }

    public synchronized void setLabenvHomeDirectory(Path path) {
        this.labenvHomeDirectory = path;
    }
}
