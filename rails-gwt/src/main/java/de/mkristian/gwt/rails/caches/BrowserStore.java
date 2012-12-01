package de.mkristian.gwt.rails.caches;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.storage.client.Storage;

import de.mkristian.gwt.rails.models.Identifyable;

public class BrowserStore<T extends Identifyable> implements Store<T>{

    private final Storage store;
    private final JsonEncoderDecoder<T> coder;
    private final String key;

    public BrowserStore(JsonEncoderDecoder<T> coder, String key){
        this.store = Storage.getLocalStorageIfSupported();
        this.coder = coder;
        this.key = key;
    }

    protected String getStoreItem(String key){
        return store == null ? null :store.getItem(key);
    }

    protected void setStoreItem(String key, String json){
        if (store != null){
            store.setItem(key, json);
        }
    }
    
    protected void removeStoreItem(String key){
        if (store != null){
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
    
    public T get(int id) {
        return getFromStore(id);
    }
    
    public List<T> getAll() {
       return getFromStore();
    }
    
    public void removeAll(){
        removeStoreItem(storeKey());
    }

    public void remove(T model){
        removeStoreItem(storeKey(model));
    }

    public void replaceAll(List<T> models, String json) {
        setStoreItem(storeKey(), json);
    }
    
    public void update(T model, String json) {
        setStoreItem(storeKey(model), json);
    }

    public void purgeAll() {
        // nothing to do here
    }
        
}