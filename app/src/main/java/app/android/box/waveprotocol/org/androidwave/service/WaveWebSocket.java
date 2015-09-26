package app.android.box.waveprotocol.org.androidwave.service;

public interface WaveWebSocket {

    interface WaveSocketCallback {
        void onConnect();
        void onDisconnect();
        void onMessage(String message);
    }

    void connect();
    void disconnect();
    void sendMessage(String message);
}
