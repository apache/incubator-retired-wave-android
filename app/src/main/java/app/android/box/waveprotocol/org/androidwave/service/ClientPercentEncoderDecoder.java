package app.android.box.waveprotocol.org.androidwave.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.waveprotocol.wave.model.id.URIEncoderDecoder;
import org.waveprotocol.wave.model.id.URIEncoderDecoder.EncodingException;

public class ClientPercentEncoderDecoder implements URIEncoderDecoder.PercentEncoderDecoder {

    @Override
    public String encode(String encodingValue) throws EncodingException {
        String encodedValue;
        try {
            return URLEncoder.encode(encodingValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException("Unable to encoding value " + encodingValue );
        }
    }

    @Override
    public String decode(String decodingValue) throws EncodingException {
        try {
            return URLDecoder.decode(decodingValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException("Unable to decoding value " + decodingValue );
        }
    }
}
