package app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol;

import org.waveprotocol.wave.concurrencycontrol.wave.CcDocument;

import app.android.box.waveprotocol.org.androidwave.service.documents.WaveDocuments;

public final class StaticChannelBinder {

    private final WaveletOperationalizer operationalizer;
    private final WaveDocuments<? extends CcDocument> docRegistry;

    public StaticChannelBinder(
            WaveletOperationalizer operationalizer, WaveDocuments<? extends CcDocument> docRegistry) {
        this.operationalizer = operationalizer;
        this.docRegistry = docRegistry;
    }
}
