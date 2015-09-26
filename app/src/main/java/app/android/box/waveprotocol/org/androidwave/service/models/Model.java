package app.android.box.waveprotocol.org.androidwave.service.models;

import com.google.common.collect.ImmutableMap;

import org.waveprotocol.wave.model.adt.ObservableBasicValue;
import org.waveprotocol.wave.model.adt.ObservableElementList;
import org.waveprotocol.wave.model.adt.docbased.DocumentBasedBasicValue;
import org.waveprotocol.wave.model.adt.docbased.DocumentBasedElementList;
import org.waveprotocol.wave.model.adt.docbased.Factory;
import org.waveprotocol.wave.model.adt.docbased.Initializer;
import org.waveprotocol.wave.model.document.Doc.E;
import org.waveprotocol.wave.model.document.ObservableDocument;
import org.waveprotocol.wave.model.document.WaveContext;
import org.waveprotocol.wave.model.document.operation.DocInitialization;
import org.waveprotocol.wave.model.document.util.DefaultDocEventRouter;
import org.waveprotocol.wave.model.document.util.DocEventRouter;
import org.waveprotocol.wave.model.document.util.DocHelper;
import org.waveprotocol.wave.model.document.util.DocProviders;
import org.waveprotocol.wave.model.document.util.DocumentEventRouter;
import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.IdUtil;
import org.waveprotocol.wave.model.id.ModernIdSerialiser;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.util.CopyOnWriteSet;
import org.waveprotocol.wave.model.util.Preconditions;
import org.waveprotocol.wave.model.util.Serializer;
import org.waveprotocol.wave.model.version.HashedVersion;
import org.waveprotocol.wave.model.wave.Blip;
import org.waveprotocol.wave.model.wave.ObservableWavelet;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.wave.SourcesEvents;
import org.waveprotocol.wave.model.wave.WaveletListener;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Model implements SourcesEvents<Model.Listener> {

    public static final String MODEL_VERSION = "0.2";
    public static final String WAVELET_ID_PREFIX = "swl";
    public static final String WAVELET_ID = WAVELET_ID_PREFIX + IdUtil.TOKEN_SEPARATOR + "root";

    public static final String ROOT_DOC_PREFIX = "model";
    private static final String ROOT_DOC_ID = ROOT_DOC_PREFIX + "+root";
    private static final String METADATA_TAG = "model";
    private static final String METADATA_ATTR_VERSION = "v";

    private static final String STRING_INDEX_TAG = "strings";
    private static final String STRING_ITEM_TAG = "s";
    private static final String STRING_VALUE_ATTR = "v";

    private static final Factory<E, ObservableBasicValue<String>, String> StringIndexFactory =
            new Factory<E, ObservableBasicValue<String>, String>() {

                @Override
                public ObservableBasicValue<String> adapt(DocumentEventRouter<? super E, E, ?> router,
                                                          E element) {
                    return DocumentBasedBasicValue.create(router, element, Serializer.STRING,
                            STRING_VALUE_ATTR);
                }

                @Override
                public Initializer createInitializer(final String initialState) {

                    return new Initializer() {

                        @Override
                        public void initialize(Map<String, String> target) {
                            target.put(STRING_VALUE_ATTR, initialState);
                        }

                    };
                }
            };

    private final MapSerializer typeSerializer;
    private final ObservableDocument rootModelDocument;
    private final ObservableElementList<ObservableBasicValue<String>, String> stringIndex;
    private final ObservableWavelet wavelet;
    private final TypeIdGenerator idGenerator;
    private final CopyOnWriteSet<Listener> listeners = CopyOnWriteSet.create();
    private final WaveletListener waveletListener = new WaveletListener() {

        @Override
        public void onParticipantRemoved(ObservableWavelet wavelet, ParticipantId participant) {
            for (Listener l : listeners)
                l.onRemoveParticipant(participant);
        }

        @Override
        public void onParticipantAdded(ObservableWavelet wavelet, ParticipantId participant) {
            for (Listener l : listeners)
                l.onAddParticipant(participant);
        }

        @Override
        public void onLastModifiedTimeChanged(ObservableWavelet wavelet, long oldTime, long newTime) {


        }

        @Override
        public void onBlipAdded(ObservableWavelet wavelet, Blip blip) {


        }

        @Override
        public void onBlipRemoved(ObservableWavelet wavelet, Blip blip) {


        }

        @Override
        public void onBlipSubmitted(ObservableWavelet wavelet, Blip blip) {


        }

        @Override
        public void onBlipTimestampModified(ObservableWavelet wavelet, Blip blip, long oldTime,
                                            long newTime) {


        }

        @Override
        public void onBlipVersionModified(ObservableWavelet wavelet, Blip blip, Long oldVersion,
                                          Long newVersion) {


        }

        @Override
        public void onBlipContributorAdded(ObservableWavelet wavelet, Blip blip,
                                           ParticipantId contributor) {


        }

        @Override
        public void onBlipContributorRemoved(ObservableWavelet wavelet, Blip blip,
                                             ParticipantId contributor) {


        }

        @Override
        public void onVersionChanged(ObservableWavelet wavelet, long oldVersion, long newVersion) {

        }

        @Override
        public void onHashedVersionChanged(ObservableWavelet wavelet, HashedVersion oldHashedVersion,
                                           HashedVersion newHashedVersion) {

        }

        @Override
        public void onRemoteBlipContentModified(ObservableWavelet wavelet, Blip blip) {

        }
    };

    private MapType rootMap = null;

    protected Model(ObservableWavelet wavelet, TypeIdGenerator idGenerator,
                    ObservableElementList<ObservableBasicValue<String>, String> stringIndex,
                    ObservableDocument modelDocument) {

        this.wavelet = wavelet;
        this.wavelet.addListener(waveletListener);

        this.idGenerator = idGenerator;
        this.stringIndex = stringIndex;
        this.rootModelDocument = modelDocument;
        this.typeSerializer = new MapSerializer(this);
    }

    public static Model create(WaveContext wave, String domain, ParticipantId loggedInUser,
                               boolean isNewWave, IdGenerator idGenerator) {

        WaveletId waveletId = WaveletId.of(domain, WAVELET_ID);
        ObservableWavelet wavelet = wave.getWave().getWavelet(waveletId);

        if (wavelet == null) {
            wavelet = wave.getWave().getWavelet(waveletId);
            wavelet.addParticipant(loggedInUser);
        }

        ObservableDocument modelDocument = wavelet.getDocument(ROOT_DOC_ID);
        DocEventRouter router = DefaultDocEventRouter.create(modelDocument);

        E metadataElement = DocHelper.getElementWithTagName(modelDocument, METADATA_TAG);
        if (metadataElement == null) {
            metadataElement = modelDocument.createChildElement(modelDocument.getDocumentElement(), METADATA_TAG,
                    ImmutableMap.of(METADATA_ATTR_VERSION, MODEL_VERSION));
        }

        E strIndexElement = DocHelper.getElementWithTagName(modelDocument, STRING_INDEX_TAG);
        if (strIndexElement == null) {
            strIndexElement =
                    modelDocument.createChildElement(modelDocument.getDocumentElement(), STRING_INDEX_TAG,
                            Collections.<String, String>emptyMap());
        }

        return new Model(wavelet, TypeIdGenerator.get(idGenerator), DocumentBasedElementList.create(
                router, strIndexElement, STRING_ITEM_TAG, StringIndexFactory), modelDocument);
    }

    protected ObservableElementList<ObservableBasicValue<String>, String> getStringIndex() {
        return stringIndex;
    }

    public WaveId getWaveId() {
        return this.wavelet.getWaveId();
    }

    public String getWaveletIdString() {
        return ModernIdSerialiser.INSTANCE.serialiseWaveletId(wavelet.getId());
    }

    protected String generateDocId(String prefix) {
        return idGenerator.newDocumentId(prefix);
    }

    protected ObservableDocument createDocument(String docId) {
        Preconditions.checkArgument(!wavelet.getDocumentIds().contains(docId),
                "Trying to create an existing substrate document");
        return wavelet.getDocument(docId);
    }

    protected DocInitialization getBlipDocInitialization(String text) {

        DocInitialization op;
        String initContent = "<body><line/>" + text + "</body>";

        try {
            op = DocProviders.POJO.parse(initContent).asOperation();
        } catch (IllegalArgumentException e) {
            /*
            if (e.getCause() instanceof XmlParseException) {

            } else {

            }
            **/
            return null;
        }

        return op;
    }

    protected Blip createBlip(String docId) {
        Preconditions.checkArgument(!wavelet.getDocumentIds().contains(docId),
                "Trying to create an existing substrate document");
        return wavelet.createBlip(docId);
    }

    protected ObservableDocument getDocument(String docId) {
        Preconditions.checkArgument(wavelet.getDocumentIds().contains(docId),
                "Trying to get a non existing substrate document");
        return wavelet.getDocument(docId);
    }


    protected Blip getBlip(String docId) {
        Preconditions.checkArgument(wavelet.getDocumentIds().contains(docId),
                "Trying to get a non existing substrate document");
        return wavelet.getBlip(docId);
    }


    protected MapSerializer getTypeSerializer() {
        return typeSerializer;
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }


    @Override
    public void removeListener(Listener listener) {
        listeners.add(listener);
    }

    public Set<ParticipantId> getParticipants() {
        return wavelet.getParticipantIds();
    }

    public void addParticipant(String address) {
        wavelet.addParticipant(ParticipantId.ofUnsafe(address));
    }

    public void removeParticipant(String address) {
        wavelet.removeParticipant(ParticipantId.ofUnsafe(address));
    }

    public MapType getRoot() {

        if (rootMap == null) {
            rootMap = (MapType) MapType.createAndAttach(this, ROOT_DOC_ID);
        }

        return rootMap;
    }


    public MapType createMap() {
        return new MapType(this);
    }

    public StringType createString(String value) {
        return new StringType(this, value);
    }

    public ListType createList() {
        return new ListType(this);
    }

    public TextType createText() {
        return new TextType(this);
    }

    public TextType createText(String textOrXml) {
        TextType tt = new TextType(this);
        if (textOrXml != null) tt.setInitContent(textOrXml);
        return tt;
    }

    public Set<String> getModelDocuments() {
        return wavelet.getDocumentIds();
    }

    public String getModelDocument(String documentId) {
        return wavelet.getDocument(documentId).toDebugString();
    }

    public interface Listener {

        void onAddParticipant(ParticipantId participant);

        void onRemoveParticipant(ParticipantId participant);

    }

}
