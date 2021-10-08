package eu.ecodex.labbox.ui.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class AppEventState {

    private final AppEventType appEventType;
    private final MetaData metaData;

    public AppEventState(AppEventType appEventType) {
        this.appEventType = appEventType;
        this.metaData = new MetaData();
    }
}
