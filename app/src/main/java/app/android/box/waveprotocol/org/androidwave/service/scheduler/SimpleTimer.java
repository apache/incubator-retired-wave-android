package app.android.box.waveprotocol.org.androidwave.service.scheduler;

import java.util.TimerTask;

public interface SimpleTimer {

    public interface Factory {
        SimpleTimer create(TimerTask runnable);
    }

    double getTime();

    void schedule();

    void schedule(double when);

    void cancel();
}
