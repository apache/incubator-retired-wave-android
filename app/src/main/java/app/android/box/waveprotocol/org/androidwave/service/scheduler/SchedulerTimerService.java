package app.android.box.waveprotocol.org.androidwave.service.scheduler;

/**
 * Created by charith on 8/12/15.
 */
public class SchedulerTimerService implements TimerService {
    public SchedulerTimerService(BrowserBackedScheduler instance, Object p1) {
    }

    @Override
    public void schedule(Scheduler.Task task) {

    }

    @Override
    public void schedule(Scheduler.IncrementalTask process) {

    }

    @Override
    public void scheduleDelayed(Scheduler.Task task, int minimumTime) {

    }

    @Override
    public void scheduleDelayed(Scheduler.IncrementalTask process, int minimumTime) {

    }

    @Override
    public void scheduleRepeating(Scheduler.IncrementalTask process, int minimumTime, int interval) {

    }

    @Override
    public void cancel(Scheduler.Schedulable job) {

    }

    @Override
    public boolean isScheduled(Scheduler.Schedulable job) {
        return false;
    }

    @Override
    public int elapsedMillis() {
        return 0;
    }

    @Override
    public double currentTimeMillis() {
        return 0;
    }
}
