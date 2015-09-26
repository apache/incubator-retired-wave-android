package app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol;

public interface Connector {

    public interface Command {

        public void execute();

    }

    void connect(Command onOpened);

    void close();
}