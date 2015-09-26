package app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol;

import com.google.common.base.Preconditions;

import org.waveprotocol.wave.model.id.ModernIdSerialiser;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.operation.wave.WaveletOperation;
import org.waveprotocol.wave.model.operation.wave.WaveletOperationContext;
import org.waveprotocol.wave.model.operation.wave.BasicWaveletOperationContextFactory;
import org.waveprotocol.wave.model.operation.wave.WaveletOperationContext.Factory;
import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.util.ReadableStringMap;
import org.waveprotocol.wave.model.util.StringMap;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.wave.ParticipationHelper;
import org.waveprotocol.wave.model.wave.data.ObservableWaveletData;
import org.waveprotocol.wave.model.wave.opbased.OpBasedWavelet;

import java.util.Collection;

public class WaveletOperationalizer {

    private final WaveId waveId;
    private final StringMap<LiveTarget<ObservableWaveletData, WaveletOperation>> wavelets =
            CollectionUtils.createStringMap();
    private final WaveletOperationContext.Factory opContextFactory;

    private WaveletOperationalizer(WaveId waveId, Factory opContextFactory) {
        this.waveId = waveId;
        this.opContextFactory = opContextFactory;
    }

    public static WaveletOperationalizer create(WaveId wave, ParticipantId user) {
        WaveletOperationContext.Factory opContexts = new BasicWaveletOperationContextFactory(user);
        return new WaveletOperationalizer(wave, opContexts);
    }

    public OpBasedWavelet operationalize(ObservableWaveletData data) {
        LiveTarget<ObservableWaveletData, WaveletOperation> target = createSinks(data);
        return new OpBasedWavelet(waveId,
                data,
                opContextFactory,
                ParticipationHelper.DEFAULT,
                target.getExecutorSink(),
                target.getOutputSink());
    }

    private LiveTarget<ObservableWaveletData, WaveletOperation> createSinks(
            ObservableWaveletData data) {
        return putAndReturn(wavelets,
                ModernIdSerialiser.INSTANCE.serialiseWaveletId(data.getWaveletId()),
                LiveTarget.<ObservableWaveletData, WaveletOperation>create(data));
    }

    private static <V> V putAndReturn(StringMap<V> map, String key, V value) {
        Preconditions.checkState(!map.containsKey(key));
        map.put(key, value);
        return value;
    }

    public Collection<ObservableWaveletData> getWavelets() {
        final Collection<ObservableWaveletData> targets = CollectionUtils.createQueue();
        this.wavelets.each(new ReadableStringMap.ProcV<LiveTarget<ObservableWaveletData, WaveletOperation>>() {
            @Override
            public void apply(String id, LiveTarget<ObservableWaveletData, WaveletOperation> triple) {
                targets.add(triple.getTarget());
            }
        });
        return targets;
    }
}
