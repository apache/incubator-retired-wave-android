package app.android.box.waveprotocol.org.androidwave.service.scheduler;

import java.util.TimerTask;

public class BrowserBackedScheduler implements Scheduler {

    private final JobRegistry jobs;
    private final SimpleTimer timer;
    private final Controller controller;
    private double nextSliceRunTime = Double.MAX_VALUE;
    private int timeSliceMillis = 100;

    public BrowserBackedScheduler(SimpleTimer.Factory timerFactory, Controller controller) {
        this.timer = timerFactory.create(runner);
        this.controller = controller;
        this.jobs = new JobRegistry(controller);
    }

    private final TimerTask runner = new TimerTask() {

        @Override
        public void run() {
            nextSliceRunTime = Double.MAX_VALUE;
            workSlice(timeSliceMillis);
            double next = getNextRunTime();
            if (next == 0) {
                maybeScheduleSlice();
            } else if (next > 0) {
                maybeScheduleSlice(next);
            }
        }
    };

    private void workSlice(int maxMillis) {

    }

    private double getNextRunTime() {
        return 0;
    }

    private void maybeScheduleSlice(){

    }

    private void maybeScheduleSlice(double when){

    }

    @Override
    public void noteUserActivity() {

    }

    @Override
    public void schedule(Priority priority, Task task) {

    }

    @Override
    public void schedule(Priority priority, IncrementalTask process) {

    }

    @Override
    public void scheduleDelayed(Priority priority, Task task, int minimumTime) {

    }

    @Override
    public void scheduleDelayed(Priority priority, IncrementalTask process, int minimumTime) {

    }

    @Override
    public void scheduleRepeating(Priority priority, IncrementalTask process, int minimumTime, int interval) {

    }

    @Override
    public void cancel(Schedulable job) {

    }

    @Override
    public boolean isScheduled(Schedulable job) {
        return false;
    }

    @Override
    public void addListener(Listener listener) {

    }

    @Override
    public void removeListener(Listener listener) {

    }

    @Override
    public String debugShortDescription() {
        return null;
    }
}
