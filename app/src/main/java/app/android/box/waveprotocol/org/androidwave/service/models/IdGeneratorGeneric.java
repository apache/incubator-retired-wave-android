package app.android.box.waveprotocol.org.androidwave.service.models;

import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.WaveId;

public interface IdGeneratorGeneric {

    IdGeneratorGeneric initialize(IdGenerator idGenerator);

    WaveId newWaveId();

}
