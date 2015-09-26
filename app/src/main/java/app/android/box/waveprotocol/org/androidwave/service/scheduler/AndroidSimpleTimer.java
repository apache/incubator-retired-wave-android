package app.android.box.waveprotocol.org.androidwave.service.scheduler;

import java.util.TimerTask;

public class AndroidSimpleTimer implements SimpleTimer {

    private final TimerTask task;

    public static final SimpleTimer.Factory FACTORY = new SimpleTimer.Factory() {
        public SimpleTimer create(TimerTask task) {
            return new AndroidSimpleTimer(task);
        }
    };

    public AndroidSimpleTimer(TimerTask task) {
        this.task = task;
    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public void schedule() {

    }

    @Override
    public void schedule(double when) {

    }

    @Override
    public void cancel() {

    }
}
