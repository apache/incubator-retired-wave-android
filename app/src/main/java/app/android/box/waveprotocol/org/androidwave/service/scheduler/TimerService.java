package app.android.box.waveprotocol.org.androidwave.service.scheduler;

import app.android.box.waveprotocol.org.androidwave.service.scheduler.Scheduler.Task;
import app.android.box.waveprotocol.org.androidwave.service.scheduler.Scheduler.IncrementalTask;
import app.android.box.waveprotocol.org.androidwave.service.scheduler.Scheduler.Schedulable;

public interface TimerService {

    void schedule(Task task);

    void schedule(IncrementalTask process);

    void scheduleDelayed(Task task, int minimumTime);

    void scheduleDelayed(IncrementalTask process, int minimumTime);

    void scheduleRepeating(IncrementalTask process, int minimumTime, int interval);

    void cancel(Schedulable job);

    boolean isScheduled(Schedulable job);

    int elapsedMillis();

    double currentTimeMillis();

}
