package app.android.box.waveprotocol.org.androidwave.service;

import org.waveprotocol.wave.concurrencycontrol.channel.WaveViewService;
import org.waveprotocol.wave.concurrencycontrol.wave.CcDataDocumentImpl;
import org.waveprotocol.wave.model.id.IdFilter;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.id.WaveletName;
import org.waveprotocol.wave.model.operation.wave.WaveletDelta;
import org.waveprotocol.wave.model.version.HashedVersion;

import java.util.List;
import java.util.Map;

import app.android.box.waveprotocol.org.androidwave.service.documents.WaveDocuments;


public class RemoteWaveViewService implements WaveViewService {

    public RemoteWaveViewService(WaveId waveId, RemoteViewServiceMultiplexer channel, WaveDocuments<CcDataDocumentImpl> documentRegistry) {
    }

    @Override
    public void viewOpen(IdFilter idFilter, Map<WaveletId, List<HashedVersion>> map, OpenCallback openCallback) {

    }

    @Override
    public String viewSubmit(WaveletName waveletName, WaveletDelta waveletDelta, String s, SubmitCallback submitCallback) {
        return null;
    }

    @Override
    public void viewClose(WaveId waveId, String s, CloseCallback closeCallback) {

    }

    @Override
    public String debugGetProfilingInfo(String s) {
        return null;
    }
}
