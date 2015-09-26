package app.android.box.waveprotocol.org.androidwave.service.models;

public abstract class Type {

    public static Type createInstance(String type, String backendId, Model model) {

        Type instance = null;

        if (StringType.PREFIX.equals(type)) {

            instance = StringType.createAndAttach(model, backendId);

        } else if (ListType.PREFIX.equals(type)) {

            instance = ListType.createAndAttach(model, backendId);

        } else if (MapType.PREFIX.equals(type)) {

            instance = MapType.createAndAttach(model, backendId);

        } else if (TextType.PREFIX.equals(type)) {

            instance = TextType.createAndAttach(model, backendId);
        }

        return instance;
    }

    protected abstract void attach(String docId);

    protected abstract void deAttach();

    protected abstract ListElementInitializer getListElementInitializer();

    protected abstract String getPrefix();

    protected abstract boolean isAttached();

    protected abstract String serializeToModel();

    public abstract String getDocumentId();

    public abstract Model getModel();

    public abstract String getType();

}
