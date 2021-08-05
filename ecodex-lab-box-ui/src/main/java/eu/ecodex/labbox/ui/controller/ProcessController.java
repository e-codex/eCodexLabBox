package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.domain.UnsupportedPlatformException;
import eu.ecodex.labbox.ui.domain.entities.Labenv;
import eu.ecodex.labbox.ui.service.PlatformService;
import lombok.Getter;
import org.springframework.stereotype.Controller;

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

    public ProcessController(PlatformService platformService) {
        this.platformService = platformService;
        this.runningProc = new HashMap<>();
    }

    public void startConnector(Labenv lab) throws IOException, UnsupportedPlatformException {
        final Path pathToExecutable = lab.getPath().resolve("domibus-connector");
        final List<String> commands = new ArrayList<>();

        if (platformService.isWindows()) {
            commands.add("cmd");
            commands.add("/c");
            // "start" opens a window, if omitted the process will only show up in task manager
            commands.add("start");
            commands.add("start.bat");

            runningProc.put("connector-" + lab.getPath().getFileName().toString(),
                    startProc(commands, pathToExecutable));

        } else if (platformService.isUnix()) {
            commands.add("bash");
            commands.add("-c");
            // TODO test if this is necessary to open windows on unix systems
            commands.add("start");
            commands.add("start.sh");
            runningProc.put("connector-" + lab.getPath().getFileName().toString(),
                    startProc(commands, pathToExecutable));
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public boolean stopConnector(Labenv labenv) {
        return stopProc("connector-" + labenv.getPath().getFileName().toString());
    }

    public void startGateway(Labenv lab) throws IOException, UnsupportedPlatformException {
        final Path pathToExecutable = lab.getPath().resolve("domibus-gateway").resolve("bin");
        final List<String> commands = new ArrayList<>();

        if (platformService.isWindows()) {
            commands.add("cmd");
            commands.add("/c");
            // "start" is not necessary because startup.bat launches its own java window anyway
            commands.add("startup.bat");

            runningProc.put("gateway-" + lab.getPath().getFileName().toString(),
                    startProc(commands, pathToExecutable));

        } else if (platformService.isUnix()) {
            commands.add("bash");
            commands.add("-c");
            commands.add("startup.sh");
            runningProc.put("gateway-" + lab.getPath().getFileName().toString(),
                    startProc(commands, pathToExecutable));
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public boolean stopGateway(Labenv labenv) {
        return stopProc("gateway-"+labenv.getPath().getFileName().toString());
    }

    public void startClient(Labenv lab) throws IOException, UnsupportedPlatformException {
        final Path pathToExecutable = lab.getPath().resolve("domibus-connector-client-application");
        final List<String> commands = new ArrayList<>();

        if (platformService.isWindows()) {
            commands.add("cmd");
            commands.add("/c");
            commands.add("start");
            commands.add("startConnectorClient.bat");

            runningProc.put("client-" + lab.getPath().getFileName().toString(),
                    startProc(commands, pathToExecutable));

        } else if (platformService.isUnix()) {
            commands.add("bash");
            commands.add("-c");
            commands.add("start");
            commands.add("startConnectorClient.sh");
            runningProc.put("client-" + lab.getPath().getFileName().toString(),
                    startProc(commands, pathToExecutable));
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    private Process startProc(List<String> commands, Path pathToExecutable) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(pathToExecutable.toFile());
        return pb.start();
    }

    private boolean stopProc(String id) {
        // TODO this does not work
        Process process = this.runningProc.get(id);
        boolean result = false;
        if (process != null) {
            process.destroy();
            result = true;
        }
        return result;
    }
}