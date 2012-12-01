package de.mkristian.gwt.rails.caches.obsolete;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.storage.client.Storage;

import de.mkristian.gwt.rails.models.Identifyable;

public abstract class AbstractStore<T extends Identifyable> extends AbstractRemoteCache<T>{

    private final Storage store;
    private final JsonEncoderDecoder<T> coder;
    private final String key;

    protected AbstractStore(EventBus eventBus, JsonEncoderDecoder<T> coder, String key){
        super(eventBus);
        this.store = Storage.getLocalStorageIfSupported();
        this.coder = coder;
        this.key = key;
    }

    private String getStoreItem(String key){
        return store == null ? null :store.getItem(key);
    }

    private void setStoreItem(String key, String json){
        if (store == null){
            store.setItem(key, json);
        }
    }
    
    private void removeStoreItem(String key){
        if (store == null){
            store.removeItem(key);
        }
    }

    private String storeKey(T model) {
        return storeKey(model.getId());
    }

    private String storeKey(int id){
        return storeKey() + id;
    }

    protected String storeKey(){
        return this.key;
    }

    protected T getFromStore(int id){
        T model = null;
        if(store != null){
            String json = store.getItem(storeKey(id));
            if (json != null){
                model = coder.decode(JSONParser.parseStrict(json));
            }
        }
        return model;
    }

    private List<T> getFromStore() {
        String json = getStoreItem(storeKey());
        if (json != null){
            JSONArray array = JSONParser.parseStrict(json).isArray();
            List<T> models = new ArrayList<T>(array.size());
            for( int i = 0; i < array.size(); i++){
                models.add(coder.decode(array.get(i)));
            }
            return models;
        }
        return null;
    }
    
    @Override
    protected void addOrReplace(T model) {
    }

    @Override
    protected void replaceAll(List<T> models) {
    }

    @Override
    protected void onLoad(Method method, T model) {
        if (model != null){
            setStoreItem(storeKey(model.getId()), method.getResponse().getText());
        }
        super.onLoad(method, model);
    }

    @Override
    public final void onLoad(Method method, List<T> models) {
        if (models != null){
            setStoreItem(storeKey(), method.getResponse().getText());
        }
        super.onLoad(method, models);
    }

    public T getModel(int id) {
        return getFromStore(id);
    }
    
    public T getOrLoadModel(int id){
        T model = getModel(id);
        if (model == null){
            loadModel(id);
            model = newModel();
        }
        return model;
    }
    
    public List<T> getModels() {
        return getFromStore();
    }
    
    public List<T> getOrLoadModels() {
        List<T> models = getModels();
        if( models == null ){
            loadModels();
        }
        return models;
    }

    public void purgeAll(){
        //  just do not delete stored items but memory cache
    }

    public void remove(T model){
        removeStoreItem(storeKey(model));
    }
    
    abstract protected void loadModels();
    abstract protected void loadModel(int id);
    abstract protected T newModel();

}