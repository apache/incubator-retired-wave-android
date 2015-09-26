package app.android.box.waveprotocol.org.androidwave.service;

import org.waveprotocol.box.common.comms.ProtocolSubmitResponse;

public interface SubmitResponseCallback {

    void run(ProtocolSubmitResponse response);
}