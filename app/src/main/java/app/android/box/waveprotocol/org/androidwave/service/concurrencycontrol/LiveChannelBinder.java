package app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol;


import org.waveprotocol.wave.concurrencycontrol.channel.Accessibility;
import org.waveprotocol.wave.concurrencycontrol.channel.OperationChannel;
import org.waveprotocol.wave.concurrencycontrol.channel.OperationChannelMultiplexer;
import org.waveprotocol.wave.concurrencycontrol.common.CorruptionDetail;
import org.waveprotocol.wave.concurrencycontrol.wave.CcDocument;
import org.waveprotocol.wave.model.id.IdFilter;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.wave.ObservableWavelet;
import org.waveprotocol.wave.model.wave.WaveViewListener;
import org.waveprotocol.wave.model.wave.data.ObservableWaveletData;
import org.waveprotocol.wave.model.wave.opbased.OpBasedWavelet;
import org.waveprotocol.wave.model.wave.opbased.WaveViewImpl;

import java.util.Collection;

import app.android.box.waveprotocol.org.androidwave.service.documents.WaveDocuments;

public final class LiveChannelBinder
        implements WaveViewListener, OperationChannelMultiplexer.Listener {

    private final StaticChannelBinder binder;
    private final WaveletOperationalizer operationalizer;
    private final WaveViewImpl<OpBasedWavelet> wave;
    private final OperationChannelMultiplexer mux;
    private final Connector.Command whenOpened;

    private LiveChannelBinder(StaticChannelBinder binder, WaveletOperationalizer operationalizer,
                              WaveViewImpl<OpBasedWavelet> wave, OperationChannelMultiplexer mux, Connector.Command whenOpened) {
        this.binder = binder;
        this.operationalizer = operationalizer;
        this.wave = wave;
        this.mux = mux;
        this.whenOpened = whenOpened;
    }

    public static void openAndBind(WaveletOperationalizer operationalizer,
                                   WaveViewImpl<OpBasedWavelet> wave,
                                   WaveDocuments<? extends CcDocument> docRegistry,
                                   OperationChannelMultiplexer mux,
                                   IdFilter filter,
                                   Connector.Command whenOpened) {

        StaticChannelBinder staticBinder = new StaticChannelBinder(operationalizer, docRegistry);

        LiveChannelBinder liveBinder =
                new LiveChannelBinder(staticBinder, operationalizer, wave, mux, whenOpened);

        final Collection<OperationChannelMultiplexer.KnownWavelet> remoteWavelets = CollectionUtils.createQueue();
        final Collection<ObservableWaveletData> localWavelets = CollectionUtils.createQueue();
        for (ObservableWaveletData wavelet : operationalizer.getWavelets()) {

            if (wavelet.getVersion() > 0) {
                remoteWavelets.add(
                        new OperationChannelMultiplexer.KnownWavelet(wavelet, wavelet.getHashedVersion(), Accessibility.READ_WRITE));
            } else {
                localWavelets.add(wavelet);
            }
        }

        wave.addListener(liveBinder);

        mux.open(liveBinder, filter, remoteWavelets);
        for (ObservableWaveletData local : localWavelets) {
            mux.createOperationChannel(local.getWaveletId(), local.getCreator());
        }
    }

    @Override
    public void onOperationChannelCreated(OperationChannel operationChannel, ObservableWaveletData observableWaveletData, Accessibility accessibility) {

    }

    @Override
    public void onOperationChannelRemoved(OperationChannel operationChannel, WaveletId waveletId) {

    }

    @Override
    public void onOpenFinished() {

    }

    @Override
    public void onFailed(CorruptionDetail corruptionDetail) {

    }

    @Override
    public void onWaveletAdded(ObservableWavelet observableWavelet) {

    }

    @Override
    public void onWaveletRemoved(ObservableWavelet observableWavelet) {

    }
}
