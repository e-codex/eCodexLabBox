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
            } else if (SystemUtils.IS_OS_UNIX) {
                OS = OSType.UNIX;
            } else {
                OS = OSType.Other;
            }
        }
        return OS;
    }

    public boolean isWindows() {
        return getOsName().equals(OSType.Windows);
    }

    public boolean isUnix() {
        return getOsName().equals(OSType.UNIX);
    }

}
