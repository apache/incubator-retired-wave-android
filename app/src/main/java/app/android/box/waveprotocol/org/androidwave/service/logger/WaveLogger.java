package app.android.box.waveprotocol.org.androidwave.service.logger;

import org.waveprotocol.wave.common.logging.AbstractLogger;
import org.waveprotocol.wave.common.logging.Logger;
import org.waveprotocol.wave.common.logging.LoggerBundle;

public class WaveLogger implements LoggerBundle {

    @Override
    public void log(AbstractLogger.Level level, Object... objects) {

    }

    @Override
    public Logger trace() {
        return null;
    }

    @Override
    public Logger error() {
        return null;
    }

    @Override
    public Logger fatal() {
        return null;
    }

    @Override
    public boolean isModuleEnabled() {
        return false;
    }
}
