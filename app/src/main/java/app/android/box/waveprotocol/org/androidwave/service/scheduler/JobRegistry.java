package app.android.box.waveprotocol.org.androidwave.service.scheduler;

import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.util.IntMap;

import java.util.LinkedList;
import java.util.Queue;

public class JobRegistry {

    private int jobCount;

    private final Controller jobCounter;

    private final IntMap<Queue<Scheduler.Schedulable>> priorities = CollectionUtils.createIntMap();

    public JobRegistry(Controller jobCounter) {
        this.jobCounter = jobCounter;

        for (Scheduler.Priority p : Scheduler.Priority.values()) {
            priorities.put(p.ordinal(), new LinkedList<Scheduler.Schedulable>());
        }
    }

}
