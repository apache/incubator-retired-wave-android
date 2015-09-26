package app.android.box.waveprotocol.org.androidwave.service.models;

import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.WaveId;

public class TypeIdGenerator implements IdGeneratorGeneric {


    public static final String WAVE_ID_PREFIX = "s";
    private static TypeIdGenerator singleton = null;
    private IdGenerator idGenerator;

    TypeIdGenerator() {

    }

    public static TypeIdGenerator get() {
        if (singleton == null) singleton = new TypeIdGenerator();
        return singleton;
    }

    public static TypeIdGenerator get(IdGenerator idGenerator) {
        if (singleton == null) singleton = new TypeIdGenerator();
        singleton.idGenerator = idGenerator;
        return singleton;
    }

    @Override
    public IdGeneratorGeneric initialize(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        return this;
    }

    @Override
    public WaveId newWaveId() {
        return WaveId.of(idGenerator.getDefaultDomain(), idGenerator.newId(WAVE_ID_PREFIX));
    }

    public String newDocumentId(String prefix) {
        return idGenerator.newId(prefix);
    }

}
