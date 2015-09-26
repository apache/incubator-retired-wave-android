package app.android.box.waveprotocol.org.androidwave.service.scheduler;

import org.waveprotocol.wave.model.util.FuzzingBackOffScheduler;
import org.waveprotocol.wave.model.util.FuzzingBackOffScheduler.CollectiveScheduler;
import org.waveprotocol.wave.model.util.Scheduler;


public class OptimalGroupingScheduler implements CollectiveScheduler {

    public OptimalGroupingScheduler(Object lowPriorityTimer) {
    }

    @Override
    public FuzzingBackOffScheduler.Cancellable schedule(Scheduler.Command command, int i, int i1) {
        return null;
    }
}
