package eu.ecodex.labbox.ui.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
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
    
    @Getter
    private Path connectorDocuSite;

    public FileAndDirectoryRepo(@Value("${spring.profiles.active}") String activeProfile, @Value("${path.to.connector.documentation.site}") String pathToConnectorDocumentationSite) {
        this.mavenExecutable = Optional.empty();

        final ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        if (activeProfile.equals("prod")) {
            this.labenvHomeDirectory = applicationHome.getDir().toPath().getParent().resolve("ecodex-lab-box");
        } else {
            this.labenvHomeDirectory = new File("C:\\Entwicklung\\ecodex-labhome-dev").toPath();
        }
        
        if(!StringUtils.isEmpty(pathToConnectorDocumentationSite)) {
        	this.connectorDocuSite = new File (this.labenvHomeDirectory + pathToConnectorDocumentationSite).toPath();
        }
    }

    public Path findExecutableOnPath(String name) {
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
