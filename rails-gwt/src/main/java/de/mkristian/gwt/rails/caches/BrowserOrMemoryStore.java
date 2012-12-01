package de.mkristian.gwt.rails.caches;

import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.storage.client.Storage;

import de.mkristian.gwt.rails.models.Identifyable;

public class BrowserOrMemoryStore<T extends Identifyable> implements Store<T> {

    private final Store<T> store;
    
    public BrowserOrMemoryStore(JsonEncoderDecoder<T> coder, String key){
        if ( Storage.isLocalStorageSupported() ){
            store = new BrowserStore<T>(coder, key);
        }
        else {
            store = new MemoryStore<T>();
        }
    }
    
    public void update(T model, String json) {
        store.update(model, json);
    }

    public void replaceAll(List<T> models, String json) {
        store.replaceAll(models, json);
    }

    public T get(int id) {
        return store.get(id);
    }

    public List<T> getAll() {
        return store.getAll();
    }

    public void remove(T model) {
        store.remove(model);
    }

    public void removeAll() {
        store.removeAll();
    }

    public void purgeAll() {
        store.purgeAll();
    }
}
