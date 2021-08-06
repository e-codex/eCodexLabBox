package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.domain.OSType;
import eu.ecodex.labbox.ui.domain.UnsupportedPlatformException;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Service;

@Service
public class PlatformService {

    private OSType OS = null;
    private String shell;
    private String scriptExtension;
    private String shellOption;

    private OSType getOSInfos() {
        if (OS == null) {
            if (SystemUtils.IS_OS_WINDOWS) {
                OS = OSType.Windows;
                shell = "cmd";
                scriptExtension = "bat";
                shellOption = "/c";
            } else if (SystemUtils.IS_OS_UNIX) {
                OS = OSType.UNIX;
                shell = "bash";
                scriptExtension = "sh";
                shellOption = "-c";
            } else {
                throw new UnsupportedPlatformException();
            }
        }
        return OS;
    }

    public boolean isWindows() {
        return getOSInfos().equals(OSType.Windows);
    }
    public boolean isUnix() {
        return getOSInfos().equals(OSType.UNIX);
    }

    public String getShell() {
        if (OS == null) {
            getOSInfos();
        }
        return shell;
    }

    public String getScriptExtension() {
        if (OS == null) {
            getOSInfos();
        }
        return scriptExtension;
    }
    public String getShellOption() {
        if (OS == null) {
            getOSInfos();
        }
        return shellOption;
    }
}
