package app.android.box.waveprotocol.org.androidwave.service;

import org.waveprotocol.box.common.comms.ProtocolWaveletUpdate;

public interface WaveWebSocketCallback {
    void onWaveletUpdate(ProtocolWaveletUpdate message);
}
