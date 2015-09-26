package app.android.box.waveprotocol.org.androidwave.service;

import org.waveprotocol.box.common.comms.ProtocolWaveletUpdate;

public class RemoteViewServiceMultiplexer implements WaveWebSocketCallback {

    private final WaveWebSocketClient socket;

    private final String userId;

    public RemoteViewServiceMultiplexer(WaveWebSocketClient socket, String userId) {
        this.socket = socket;
        this.userId = userId;

        socket.attachHandler(this);
    }

    @Override
    public void onWaveletUpdate(ProtocolWaveletUpdate message) {

    }
}