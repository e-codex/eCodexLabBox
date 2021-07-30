package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.domain.OSType;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Service;

@Service
public class PlatformService {

    private OSType OS = null;

    private OSType getOsName() {
        if (OS == null) {
            if (SystemUtils.IS_OS_WINDOWS) {
                OS = OSType.Windows;
            } else if (SystemUtils.IS_OS_LINUX) {
                OS = OSType.Linux;
            } else if (SystemUtils.IS_OS_MAC) {
                OS = OSType.MacOS;
            } else {
                OS = OSType.Other;
            }
        }
        return OS;
    }

    public boolean isWindows() {
        return getOsName().equals(OSType.Windows);
    }

    public boolean isLinux() {
        return getOsName().equals(OSType.Linux);
    }

    public boolean isMacOS() {
        return getOsName().equals(OSType.MacOS);
    }
}
