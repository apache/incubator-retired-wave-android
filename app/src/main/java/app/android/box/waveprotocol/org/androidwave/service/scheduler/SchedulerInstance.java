package app.android.box.waveprotocol.org.androidwave.service.scheduler;



public class SchedulerInstance {

    private static TimerService low;
    private static TimerService high;
    private static TimerService medium;

    private static BrowserBackedScheduler instance;

    private static void init() {
        if (instance == null) {
            setSchedulerInstance(new BrowserBackedScheduler(AndroidSimpleTimer.FACTORY, Controller.NOOP));
        }
    }

    public static void setSchedulerInstance(BrowserBackedScheduler instance) {
        SchedulerInstance.instance = instance;
        setDefaultTimerService();
    }

    private static void setDefaultTimerService() {
        low = new SchedulerTimerService(instance, Scheduler.Priority.LOW);
        high = new SchedulerTimerService(instance, Scheduler.Priority.HIGH);
        medium = new SchedulerTimerService(instance, Scheduler.Priority.MEDIUM);
    }

    public static TimerService getLowPriorityTimer() {
        init();
        return low;
    }
}
