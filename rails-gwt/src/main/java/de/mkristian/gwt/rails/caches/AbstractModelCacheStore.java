package de.mkristian.gwt.rails.caches;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.storage.client.Storage;

import de.mkristian.gwt.rails.models.Identifyable;

public abstract class AbstractModelCacheStore<T extends Identifyable> extends AbstractModelCache<T>{
    private final Storage store;
    private final JsonEncoderDecoder<T> coder;
    private final String key;

    protected AbstractModelCacheStore(EventBus eventBus, JsonEncoderDecoder<T> coder, String key){
        super(eventBus);
        this.store = Storage.getLocalStorageIfSupported();
        this.coder = coder;
        this.key = key;
    }
    
    @Override
    public List<T> getOrLoadModels() {
        List<T> models = super.getOrLoadModels();
        if(store != null && models == null){
            String json = store.getItem(storeKey());
            if (json != null){
                JSONArray array = JSONParser.parseStrict(json).isArray();
                models = new ArrayList<T>(array.size());
                for( int i = 0; i < array.size(); i++){
                    models.add(coder.decode(array.get(i)));
                }
                super.onLoad(null, models);
            }
        }
        return models;
    }
    
    protected void doUpdate(Method method, T model) {
        super.onUpdate(null, model);
        if (store != null){
            store.setItem(storeKey(model), method.getResponse().getText()); 
        }
    }
    
    protected void doCreate(Method method, T model) {
        super.onCreate(null, model);
        if (store != null){
            store.setItem(storeKey(model), method.getResponse().getText());
        }
    }

    protected void doDestroy(T model) {
        super.onDestroy(null, model);
        if (store != null){
            store.removeItem(storeKey(model));
        }
    }

    protected void doLoad(Method method, List<T> models) {
        super.onLoad(method, models);
        if (models != null && store != null){
            store.setItem(storeKey(), method.getResponse().getText());
        }
    }

    protected void doLoad(Method method, T model) {
        super.onLoad(method, model);
        if (model != null && store != null){
            store.setItem(storeKey(model.getId()), method.getResponse().getText());
        }
    }

    @Override
    public final void onLoad(Method method, List<T> models) {
        doLoad(method, models);
    }
    
    @Override
    public final void onLoad(Method method, T model) {
        doLoad(method, model);
    }
    
    @Override
    public final void onCreate(Method method, T model) {
        doCreate(method, model);
    }

    @Override
    public final void onUpdate(Method method, T model) {
        doUpdate(method, model);
    }

    @Override
    public final void onDestroy(Method method, T model) {
        doDestroy(model);
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

    public final T getModel(int id) {
        return doGetModel(id);
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
    
    protected T doGetModel(int id) {
        T model = super.getModel(id);
        if(model.getId() != id){
            model = getFromStore(id);
        }
        return model;
    }

}