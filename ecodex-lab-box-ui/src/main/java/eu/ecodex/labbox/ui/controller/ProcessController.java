package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.configuration.WatchDirectoryConfig;
import eu.ecodex.labbox.ui.domain.Labenv;
import eu.ecodex.labbox.ui.domain.UnsupportedPlatformException;
import eu.ecodex.labbox.ui.service.PlatformService;
import lombok.Getter;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProcessController {

    @Getter
    Map<String, Process> runningProc;

    final PlatformService platformService;
    final WatchDirectoryConfig directoryConfig;

    public ProcessController(PlatformService platformService, WatchDirectoryConfig directoryConfig) {
        this.platformService = platformService;
        this.directoryConfig = directoryConfig;
        this.runningProc = new HashMap<>();
    }

    public void startConnector(Labenv lab) throws IOException, UnsupportedPlatformException {
        // File.seperator is either \ or / depending on platform
        // File.pathSeperator is used to seperate Paths in a String of Paths, on windows it is a ;
        // -> pathSeperator means to seperate paths e.g.: C:\dev;D:\elsewhere
        final String relPathToExecutable = lab.getFolderName() + File.separator + "domibus-connector";
        final List<String> commands = new ArrayList<>();

        if (platformService.isWindows()) {
            commands.add("cmd");
            commands.add("/c");
            // "start" opens a window, if omitted the process will only show up in task manager
            commands.add("start");
            commands.add("start.bat");

            runningProc.put("connector-" + lab.getFolderName(),
                    startProc(commands, relPathToExecutable));

        } else if (platformService.isUnix()) {
            commands.add("bash");
            commands.add("-c");
            // TODO test if this is necessary to open windows on unix systems
            commands.add("start");
            commands.add("start.sh");
            runningProc.put("connector-" + lab.getFolderName(),
                    startProc(commands, relPathToExecutable));
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public boolean stopConnector(Labenv labenv) {
        return stopProc("connector-" + labenv.getFolderName());
    }

    public void startGateway(Labenv lab) throws IOException, UnsupportedPlatformException {
        final String relPathToExecutable = lab.getFolderName() + File.separator + "domibus-gateway" + File.separator + "bin";
        final List<String> commands = new ArrayList<>();

        if (platformService.isWindows()) {
            commands.add("cmd");
            commands.add("/c");
            // "start" is not necessary because startup.bat launches its own java window anyway
            commands.add("startup.bat");

            runningProc.put("gateway-" + lab.getFolderName(),
                    startProc(commands, relPathToExecutable));

        } else if (platformService.isUnix()) {
            commands.add("bash");
            commands.add("-c");
            commands.add("startup.sh");
            runningProc.put("gateway-" + lab.getFolderName(),
                    startProc(commands, relPathToExecutable));
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public boolean stopGateway(Labenv labenv) {
        return stopProc("gateway-"+labenv.getFolderName());
    }

    public void startClient(Labenv lab) throws IOException, UnsupportedPlatformException {
        final String relPathToExecutable = lab.getFolderName() + File.separator + "domibus-connector-client-application";
        final List<String> commands = new ArrayList<>();

        if (platformService.isWindows()) {
            commands.add("cmd");
            commands.add("/c");
            commands.add("start");
            commands.add("startConnectorClient.bat");

            runningProc.put("client-" + lab.getFolderName(),
                    startProc(commands, relPathToExecutable));

        } else if (platformService.isUnix()) {
            commands.add("bash");
            commands.add("-c");
            commands.add("start");
            commands.add("startConnectorClient.sh");
            runningProc.put("client-" + lab.getFolderName(),
                    startProc(commands, relPathToExecutable));
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    private Process startProc(List<String> commands, String relPathToExectutable) throws IOException, UnsupportedPlatformException {
        Path labhome = directoryConfig.getLabenvHomeDirectory();
        Path pathToEcectuable = labhome.resolve(relPathToExectutable);
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(pathToEcectuable.toFile());
        return pb.start();
    }

    private boolean stopProc(String id) {
        Process process = this.runningProc.get(id);
        boolean result = false;
        if (process != null) {
            process.destroy();
            result = true;
        }
        return result;
    }
}