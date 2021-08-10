package eu.ecodex.labbox.ui.domain.entities;

import java.util.Comparator;

public class LabenvComparator implements Comparator<Labenv> {
    @Override
    public int compare(Labenv l1, Labenv l2) {
        return l1.getPath().getFileName().toString().compareTo(l2.getPath().getFileName().toString());
    }
}
