package app.android.box.waveprotocol.org.androidwave.service.scheduler;

public interface Controller {

    void jobAdded(Scheduler.Priority priority, Scheduler.Schedulable job);

    void jobRemoved(Scheduler.Priority priority, Scheduler.Schedulable job);

    boolean isRunnable(Scheduler.Priority priority);

    boolean isSuppressed(Scheduler.Priority priority, Scheduler.Schedulable job);

    public static final Controller NOOP = new Controller() {


        @Override
        public void jobAdded(Scheduler.Priority priority, Scheduler.Schedulable job) {
            // Do nothing
        }

        @Override
        public void jobRemoved(Scheduler.Priority priority, Scheduler.Schedulable job) {
            // Do nothing
        }

        @Override
        public boolean isRunnable(Scheduler.Priority priority) {
            return true;
        }

        @Override
        public boolean isSuppressed(Scheduler.Priority priority, Scheduler.Schedulable job) {
            return false;
        }
    };
}
