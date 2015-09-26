package app.android.box.waveprotocol.org.androidwave.service.scheduler;

public interface Scheduler {

    public interface Listener {
        void onJobExecuted(Schedulable job, int timeSpent);
    }

    public interface Schedulable {
    }

    public interface Task extends Schedulable {

        void execute();
    }

    public interface IncrementalTask extends Schedulable {

        boolean execute();
    }

    public enum Priority {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INTERNAL_SUPPRESS;
    }

    public void noteUserActivity();

    void schedule(Priority priority, Task task);

    void schedule(Priority priority, IncrementalTask process);

    void scheduleDelayed(Priority priority, Task task, int minimumTime);

    void scheduleDelayed(Priority priority, IncrementalTask process,
                         int minimumTime);

    void scheduleRepeating(Priority priority, IncrementalTask process,
                           int minimumTime, int interval);

    void cancel(Schedulable job);

    boolean isScheduled(Schedulable job);

    void addListener(Listener listener);

    void removeListener(Listener listener);

    public String debugShortDescription();
}
