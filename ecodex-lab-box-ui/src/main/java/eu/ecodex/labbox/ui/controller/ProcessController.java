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

    public void startConnector(Labenv lab) throws IOException {
        final Path pathToExecutable = lab.getPath().resolve("domibus-connector");
        final List<String> commands = new ArrayList<>();

        commands.add(platformService.getShell());
        commands.add(platformService.getShellOption());
        // "start" opens a window, if omitted the process will only show up in task manager
        commands.add("start"); // TODO maybe unecessary / not working on unix platforms, needs to be tested
        commands.add("start." + platformService.getScriptExtension());

        runningProc.put("connector-" + lab.getPath().getFileName().toString(),
                run(commands, pathToExecutable));

    }

    public void stopConnector(Labenv labenv) {
        stopProc("connector-" + labenv.getPath().getFileName().toString());
    }

    public void startGateway(Labenv lab) throws IOException {
        controlGateway(lab, true);
    }

    private void controlGateway(Labenv lab, boolean startStop) throws IOException {
        final Path pathToExecutable = lab.getPath().resolve("domibus-gateway").resolve("bin");
        final List<String> commands = new ArrayList<>();

        commands.add(platformService.getShell());
        commands.add(platformService.getShellOption());
        // "start" is not necessary because startup.bat launches its own java window anyway
        if (startStop) {
            commands.add("startup." + platformService.getScriptExtension());
            runningProc.put("gateway-" + lab.getPath().getFileName().toString(),
                    run(commands, pathToExecutable));
        } else {
            commands.add("shutdown." + platformService.getScriptExtension());
            run(commands, pathToExecutable);
            runningProc.remove("gateway-" + lab.getPath().getFileName().toString());
        }
    }

    public void stopGateway(Labenv labenv) throws IOException {
        controlGateway(labenv, false);
    }

    public void startClient(Labenv lab) throws IOException {
        final Path pathToExecutable = lab.getPath().resolve("domibus-connector-client-application");
        final List<String> commands = new ArrayList<>();

        commands.add(platformService.getShell());
        commands.add(platformService.getShellOption());
        commands.add("start");
        commands.add("startConnectorClient." + platformService.getScriptExtension());

        runningProc.put("client-" + lab.getPath().getFileName().toString(),
                run(commands, pathToExecutable));
    }

    public void stopClient(Labenv labenv) {
        stopProc("client-" + labenv.getPath().getFileName().toString());
    }

    private Process run(List<String> commands, Path pathToExecutable) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(pathToExecutable.toFile());
        return pb.start();
    }

    private void stopProc(String id) {

        // TODO this worked once, now it does note ???
        // TODO this is unreliable
        Process process = this.runningProc.get(id);
        boolean result = false;
        if (process != null) {
            // process.destroy(); // this does not work
            try {
                process.destroyForcibly().waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // TODO this works but closes all Connector windows
        try {
            // TODO CONTINUE HERE
            // TODO find a way to start the process with a particular name/id and terminate that
            Runtime.getRuntime().exec("taskkill /fi \"WINDOWTITLE eq \\\"DomibusConn*\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}