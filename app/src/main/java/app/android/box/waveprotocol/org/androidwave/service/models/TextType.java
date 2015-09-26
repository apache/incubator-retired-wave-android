package app.android.box.waveprotocol.org.androidwave.service.models;

import org.waveprotocol.wave.model.document.AnnotationInterval;
import org.waveprotocol.wave.model.document.Doc.N;
import org.waveprotocol.wave.model.document.Document;
import org.waveprotocol.wave.model.document.ObservableDocument;
import org.waveprotocol.wave.model.document.util.Point;
import org.waveprotocol.wave.model.document.util.XmlStringBuilder;
import org.waveprotocol.wave.model.util.CopyOnWriteSet;
import org.waveprotocol.wave.model.util.Preconditions;
import org.waveprotocol.wave.model.wave.Blip;
import org.waveprotocol.wave.model.wave.SourcesEvents;

import java.util.Collections;

public class TextType extends Type implements SourcesEvents<TextType.Listener> {

    public final static String PREFIX = "b";
    public final static String VALUE_ATTR = "t";
    private final CopyOnWriteSet<Listener> listeners = CopyOnWriteSet.create();
    private Model model;
    private Blip blip;
    private String initContent;
    private boolean isAttached;

    protected TextType(Model model) {
        this.model = model;
        this.isAttached = false;
    }

    protected static Type createAndAttach(Model model, String id) {
        Preconditions.checkArgument(id.startsWith(PREFIX), "Not a TextType instance id");
        TextType txt = new TextType(model);
        txt.attach(id);
        return txt;
    }

    protected void setInitContent(String textOrXml) {
        this.initContent = textOrXml;
    }

    @Override
    protected void attach(String docId) {

        if (docId == null) {
            docId = model.generateDocId(PREFIX);
            blip = model.createBlip(docId);

            if (initContent == null)
                initContent = "";
            XmlStringBuilder sb = XmlStringBuilder.createFromXmlString("<body><line/>" + this.initContent + "</body>");
            blip.getContent().appendXml(sb);

        } else {
            blip = model.getBlip(docId);
        }
        Preconditions.checkNotNull(blip, "Unable to attach TextType, couldn't create or get blip");
        isAttached = true;
    }

    @Override
    protected void deAttach() {
        Preconditions.checkArgument(isAttached, "Unable to deAttach an unattached TextType");
    }

    @Override
    protected ListElementInitializer getListElementInitializer() {
        return new ListElementInitializer() {

            @Override
            public String getType() {
                return PREFIX;
            }

            @Override
            public String getBackendId() {
                return serializeToModel();
            }
        };
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    @Override
    protected boolean isAttached() {
        return isAttached;
    }

    @Override
    protected String serializeToModel() {
        Preconditions.checkArgument(isAttached, "Unable to serialize an unattached TextType");
        return blip.getId();
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public String getDocumentId() {
        return blip.getId();
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public String getType() {
        return "TextType";
    }

    public ObservableDocument getMutableDocument() {
        return blip.getWavelet().getDocument(blip.getId());
    }

    public void insertText(int location, String text) {
        Document doc = blip.getContent();
        doc.insertText(location, text);
    }

    public void insertNewLine(int location) {
        Document doc = blip.getContent();
        Point<N> point = doc.locate(location);
        doc.createElement(point, "line", Collections.<String, String>emptyMap());
    }

    public void deleteText(int start, int end) {
        Document doc = blip.getContent();
        doc.deleteRange(start, end);
    }

    public int getSize() {
        Document doc = blip.getContent();
        return doc.size();
    }

    public String getXml() {
        Document doc = blip.getContent();
        return doc.toXmlString();
    }

    public void setAnnotation(int start, int end, String key, String value) {
        Document doc = blip.getContent();
        doc.setAnnotation(start, end, key, value);
    }

    public String getAnnotation(int location, String key) {
        Document doc = blip.getContent();
        return doc.getAnnotation(location, key);
    }

    public Iterable<AnnotationInterval<String>> getAllAnnotations(int start, int end) {
        Document doc = blip.getContent();
        return doc.annotationIntervals(start, end, null);
    }

    public interface Listener {

    }

}
