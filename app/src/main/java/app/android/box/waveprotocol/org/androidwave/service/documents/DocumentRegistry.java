package app.android.box.waveprotocol.org.androidwave.service.documents;

import org.waveprotocol.wave.model.conversation.ConversationBlip;

public interface DocumentRegistry<D> {
    D get(ConversationBlip blip);
}

