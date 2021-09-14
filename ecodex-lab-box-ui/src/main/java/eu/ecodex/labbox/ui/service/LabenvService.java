package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.domain.entities.Labenv;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class LabenvService {

    private Path lab;
    private Map<Path, Labenv> labenvironments;

    public LabenvService() {
        this.labenvironments = new HashMap<>();
    }

    public synchronized Map<Path, Labenv> getLabenvironments() {
        return labenvironments;
    }

    public synchronized void setLabenvironments(Map<Path, Labenv> labenvironments) {
        this.labenvironments = labenvironments;
    }

    public synchronized Path getLab() {
        return lab;
    }

    public synchronized void setLab(Path lab) {
        this.lab = lab;
    }
}
