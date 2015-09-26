package app.android.box.waveprotocol.org.androidwave.service;

import com.google.common.base.Preconditions;

import org.waveprotocol.wave.concurrencycontrol.channel.OperationChannelMultiplexer;
import org.waveprotocol.wave.concurrencycontrol.channel.OperationChannelMultiplexerImpl;
import org.waveprotocol.wave.concurrencycontrol.channel.ViewChannelFactory;
import org.waveprotocol.wave.concurrencycontrol.channel.ViewChannelImpl;
import org.waveprotocol.wave.concurrencycontrol.channel.WaveViewService;
import org.waveprotocol.wave.concurrencycontrol.common.UnsavedDataListener;
import org.waveprotocol.wave.concurrencycontrol.common.UnsavedDataListenerFactory;
import org.waveprotocol.wave.concurrencycontrol.wave.CcDataDocumentImpl;
import org.waveprotocol.wave.model.conversation.ObservableConversationView;
import org.waveprotocol.wave.model.conversation.WaveBasedConversationView;
import org.waveprotocol.wave.model.document.WaveContext;
import org.waveprotocol.wave.model.document.indexed.IndexedDocumentImpl;
import org.waveprotocol.wave.model.document.operation.DocInitialization;
import org.waveprotocol.wave.model.document.operation.automaton.DocumentSchema;
import org.waveprotocol.wave.model.id.IdConstants;
import org.waveprotocol.wave.model.id.IdFilter;
import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.IdURIEncoderDecoder;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.schema.SchemaProvider;
import org.waveprotocol.wave.model.schema.conversation.ConversationSchemas;
import org.waveprotocol.wave.model.util.FuzzingBackOffScheduler;
import org.waveprotocol.wave.model.util.FuzzingBackOffScheduler.CollectiveScheduler;
import org.waveprotocol.wave.model.version.HashedVersion;
import org.waveprotocol.wave.model.version.HashedVersionFactory;
import org.waveprotocol.wave.model.version.HashedVersionZeroFactoryImpl;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.wave.data.ObservableWaveletData;
import org.waveprotocol.wave.model.wave.data.WaveViewData;
import org.waveprotocol.wave.model.wave.data.impl.ObservablePluggableMutableDocument;
import org.waveprotocol.wave.model.wave.data.impl.WaveViewDataImpl;
import org.waveprotocol.wave.model.wave.data.impl.WaveletDataImpl;
import org.waveprotocol.wave.model.wave.data.DocumentFactory;
import org.waveprotocol.wave.model.wave.opbased.OpBasedWavelet;
import org.waveprotocol.wave.model.wave.opbased.WaveViewImpl;
import org.waveprotocol.wave.model.wave.opbased.WaveViewImpl.WaveletFactory;
import org.waveprotocol.wave.model.wave.opbased.WaveViewImpl.WaveletConfigurator;
import org.waveprotocol.wave.model.waveref.WaveRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.Timer;

import app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol.Connector;
import app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol.Connector.Command;
import app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol.LiveChannelBinder;
import app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol.WaveletOperationalizer;
import app.android.box.waveprotocol.org.androidwave.service.documents.WaveDocuments;
import app.android.box.waveprotocol.org.androidwave.service.logger.WaveLogger;
import app.android.box.waveprotocol.org.androidwave.service.models.Model;
import app.android.box.waveprotocol.org.androidwave.service.scheduler.OptimalGroupingScheduler;
import app.android.box.waveprotocol.org.androidwave.service.scheduler.Scheduler;
import app.android.box.waveprotocol.org.androidwave.service.scheduler.SchedulerInstance;

public class WaveRender {

    private final WaveRef waveRef;
    private final boolean isNewWave;
    private final Set<ParticipantId> otherParticipants;
    private ParticipantId signedInuser;
    private IdGenerator idGenerator;

    private final RemoteViewServiceMultiplexer channel;
    private final UnsavedDataListener unsavedDataListener;

    private WaveViewData waveData;
    private Connector connector;
    private WaveContext waveContext;

    private ObservableConversationView conversations;
    private WaveViewImpl<OpBasedWavelet> wave;
    private WaveletOperationalizer wavelets;
    private WaveDocuments<CcDataDocumentImpl> documentRegistry;

    private CollectiveScheduler rpcScheduler;

    private boolean isClosed = true;
    private Timer timer;


    public WaveRender(boolean isNewWave, WaveRef waveRef, RemoteViewServiceMultiplexer waveChannel,
                      ParticipantId waveParticipant,
                      Set<ParticipantId> waveOtherParticipants, IdGenerator waveIdGenerator,
                      UnsavedDataListener unsavedDataListener, Timer timer) {
        this.signedInuser = waveParticipant;
        this.waveRef = waveRef;
        this.isNewWave = isNewWave;
        this.idGenerator = waveIdGenerator;
        this.channel = waveChannel;
        this.otherParticipants = waveOtherParticipants;
        this.unsavedDataListener = unsavedDataListener;
        this.timer = timer;
    }

    public void init(Command command) {

        waveData = WaveViewDataImpl.create(waveRef.getWaveId());

        if (isNewWave) {

            getConversations().createRoot().addParticipantIds(otherParticipants);
            getConnector().connect(command);
        } else {
            getConnector().connect(command);
        }

        isClosed = false;
    }

    private ObservableConversationView getConversations() {
        return conversations == null ? conversations = createConversations() : conversations;
    }

    private Connector getConnector() {
        return connector == null ? connector = createConnector() : connector;
    }

    private ObservableConversationView createConversations() {
        return WaveBasedConversationView.create(getWave(), getIdGenerator());
    }

    private WaveViewImpl<OpBasedWavelet> getWave() {
        return wave == null ? wave = createWave() : wave;
    }

    private IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public WaveContext getWaveContext() {

        if (isClosed) {
            return null;
        }
        if (waveContext == null) {
            waveContext = new WaveContext(getWave(), getConversations(), null, null);
        }

        return waveContext;
    }

    private WaveViewImpl<OpBasedWavelet> createWave() {

        WaveViewData snapshot = getWaveData();

        final WaveletOperationalizer operationalizer = getWavelets();
        WaveletFactory<OpBasedWavelet> waveletFactory = new WaveletFactory<OpBasedWavelet>() {
            @Override
            public OpBasedWavelet create(WaveId waveId, WaveletId id, ParticipantId creator) {
                long now = System.currentTimeMillis();
                ObservableWaveletData data = new WaveletDataImpl(id, creator, now, 0L,
                        HashedVersion.unsigned(0), now, waveId, getDocumentRegistry());
                return operationalizer.operationalize(data);
            }
        };
        WaveViewImpl<OpBasedWavelet> wave = WaveViewImpl.create(waveletFactory, snapshot.getWaveId(),
                getIdGenerator(), getSignedInUser(), WaveletConfigurator.ADD_CREATOR);

        for (ObservableWaveletData waveletData : snapshot.getWavelets()) {
            wave.addWavelet(operationalizer.operationalize(waveletData));
        }
        return wave;
    }

    private WaveViewData getWaveData() {
        Preconditions.checkState(waveData != null, "wave not ready");
        return waveData;
    }

    private WaveletOperationalizer getWavelets() {
        return wavelets == null ? wavelets = createWavelets() : wavelets;
    }

    private WaveletOperationalizer createWavelets() {
        return WaveletOperationalizer.create(getWaveData().getWaveId(), getSignedInUser());
    }

    private ParticipantId getSignedInUser() {
        return signedInuser;
    }

    private WaveDocuments<CcDataDocumentImpl> getDocumentRegistry() {
        return documentRegistry == null
                ? documentRegistry = createDocumentRegistry() : documentRegistry;
    }

    private WaveDocuments<CcDataDocumentImpl> createDocumentRegistry() {
        IndexedDocumentImpl.performValidation = false;

        DocumentFactory<?> dataDocFactory =
                ObservablePluggableMutableDocument.createFactory(createSchemas());

        DocumentFactory<CcDataDocumentImpl> fakeBlipDocFactory = new DocumentFactory<CcDataDocumentImpl>() {

            @Override
            public CcDataDocumentImpl create(WaveletId waveletId, String docId, DocInitialization content) {
                return new CcDataDocumentImpl(DocumentSchema.NO_SCHEMA_CONSTRAINTS, content);
            }

        };

        return WaveDocuments.create(fakeBlipDocFactory, dataDocFactory);
    }

    private SchemaProvider createSchemas() {
        return new ConversationSchemas();
    }

    private Connector createConnector() {

        WaveLogger loggerView = new WaveLogger();

        IdURIEncoderDecoder uriCodec = new IdURIEncoderDecoder(new ClientPercentEncoderDecoder());
        HashedVersionFactory hashFactory = new HashedVersionZeroFactoryImpl(uriCodec);

        Scheduler scheduler = (Scheduler) new FuzzingBackOffScheduler.Builder(getRpcScheduler())
                .setInitialBackOffMs(1000).setMaxBackOffMs(60000).setRandomisationFactor(0.5).build();

        ViewChannelFactory viewFactory = ViewChannelImpl.factory(createWaveViewService(), loggerView);

        UnsavedDataListenerFactory unsyncedListeners = new UnsavedDataListenerFactory() {

            private final UnsavedDataListener listener = unsavedDataListener;

            @Override
            public UnsavedDataListener create(WaveletId waveletId) {
                return listener;
            }

            @Override
            public void destroy(WaveletId waveletId) {

            }
        };

        WaveletId udwId = getIdGenerator().newUserDataWaveletId(getSignedInUser().getAddress());

        ArrayList<String> prefixes = new ArrayList<String>();
        prefixes.add(IdConstants.CONVERSATION_WAVELET_PREFIX);
        prefixes.add(Model.WAVELET_ID_PREFIX);

        final IdFilter filter = IdFilter.of(Collections.singleton(udwId), prefixes);

        OperationChannelMultiplexerImpl.LoggerContext loggers = new OperationChannelMultiplexerImpl.LoggerContext(loggerView, loggerView,loggerView, loggerView);

        WaveletDataImpl.Factory snapshotFactory = WaveletDataImpl.Factory.create(getDocumentRegistry());
        final OperationChannelMultiplexer mux = new OperationChannelMultiplexerImpl(getWave()
                .getWaveId(), viewFactory, snapshotFactory, loggers, unsyncedListeners, (org.waveprotocol.wave.model.util.Scheduler) scheduler,
                hashFactory);

        final WaveViewImpl<OpBasedWavelet> wave = getWave();


        return new Connector() {
            @Override
            public void connect(Command onOpened) {
                LiveChannelBinder.openAndBind(getWavelets(), wave, getDocumentRegistry(), mux, filter,
                        onOpened);
            }

            @Override
            public void close() {
                mux.close();
            }
        };
    }

    private CollectiveScheduler getRpcScheduler() {
        return rpcScheduler == null ? rpcScheduler = createRpcScheduler() : rpcScheduler;
    }

    protected WaveViewService createWaveViewService() {
        return new RemoteWaveViewService(waveRef.getWaveId(), channel, getDocumentRegistry());
    }

    protected CollectiveScheduler createRpcScheduler() {
        return new OptimalGroupingScheduler(SchedulerInstance.getLowPriorityTimer());
    }
}
