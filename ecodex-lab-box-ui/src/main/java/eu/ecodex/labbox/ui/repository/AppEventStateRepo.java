package eu.ecodex.labbox.ui.repository;

import com.vaadin.flow.component.notification.Notification;
import eu.ecodex.labbox.ui.domain.AppEventState;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class AppEventStateRepo {

    // It's possible to only use the map activeNotifications to provide the functionality.
    // On startup one would check, if the key exists in the map and if the value is not null.
    // no entry -> no AppState set
    // only key -> AppState set, but not yet handled
    // key & value -> AppState set, Notification active
    // but I feel somehow uncomfortable mapping keys to null, in the case of clearing active notifications
    // and I don't have the time right now
    private final Set<AppEventState> appEventState;

    // this map gets cleared when the MainLayout is created
    // creation of the MainLayout happens only initially and after browser/page refresh
    private final Map<String, Notification> activeNotifications;

    public AppEventStateRepo() {
        this.appEventState = new HashSet<>();
        this.activeNotifications = new HashMap<>();
    }

    public synchronized Map<String, Notification> getActiveNotifications() {
        return this.activeNotifications;
    }

    public synchronized Set<AppEventState> getAppEventState() {
        return this.appEventState;
    }
}
