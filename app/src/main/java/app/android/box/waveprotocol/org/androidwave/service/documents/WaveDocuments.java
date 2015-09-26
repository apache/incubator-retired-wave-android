package app.android.box.waveprotocol.org.androidwave.service.documents;

import com.google.common.base.Preconditions;

import org.waveprotocol.wave.model.conversation.ConversationBlip;
import org.waveprotocol.wave.model.document.operation.DocInitialization;
import org.waveprotocol.wave.model.id.IdUtil;
import org.waveprotocol.wave.model.id.ModernIdSerialiser;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.util.StringMap;
import org.waveprotocol.wave.model.wave.data.DocumentFactory;
import org.waveprotocol.wave.model.wave.data.DocumentOperationSink;

public final class WaveDocuments<BlipDocument extends DocumentOperationSink>
        implements DocumentFactory<DocumentOperationSink>, DocumentRegistry<BlipDocument> {

    private final DocumentFactory<BlipDocument> blipDocFactory;
    private final DocumentFactory<?> dataDocFactory;
    private final StringMap<StringMap<BlipDocument>> blips = CollectionUtils.createStringMap();

    private WaveDocuments(DocumentFactory<BlipDocument> blip, DocumentFactory<?> data) {
        this.blipDocFactory = blip;
        this.dataDocFactory = data;
    }

    public static <B extends DocumentOperationSink> WaveDocuments<B> create(
            DocumentFactory<B> blipDocFactory, DocumentFactory<?> dataDocFactory) {
        return new WaveDocuments<B>(blipDocFactory, dataDocFactory);
    }

    @Override
    public DocumentOperationSink create(
            final WaveletId waveletId, final String blipId, final DocInitialization content) {

        String waveletIdStr = ModernIdSerialiser.INSTANCE.serialiseWaveletId(waveletId);
        if (IdUtil.isBlipId(blipId)) {
            BlipDocument document = blipDocFactory.create(waveletId, blipId, content);
            StringMap<BlipDocument> convDocuments = getConversationDocuments(waveletIdStr);
            Preconditions.checkState(!convDocuments.containsKey(blipId));
            convDocuments.put(blipId, document);
            return document;
        } else {
            return dataDocFactory.create(waveletId, blipId, content);
        }
    }

    private StringMap<BlipDocument> getConversationDocuments(String id) {
        StringMap<BlipDocument> convDocuments = blips.get(id);
        if (convDocuments == null) {
            convDocuments = CollectionUtils.createStringMap();
            blips.put(id, convDocuments);
        }
        return convDocuments;
    }

    public BlipDocument get(ConversationBlip blip) {
        return getBlipDocument(blip.getConversation().getId(), blip.getId());
    }

    public BlipDocument getBlipDocument(String waveletId, String docId) {
        StringMap<BlipDocument> convDocuments = blips.get(waveletId);
        return convDocuments != null ? convDocuments.get(docId) : null;
    }
}
