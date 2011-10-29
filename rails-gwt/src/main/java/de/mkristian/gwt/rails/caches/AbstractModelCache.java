package de.mkristian.gwt.rails.caches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.session.SessionManager;

public abstract class AbstractModelCache<T extends Identifyable> implements Cache {
    
    private final Map<Integer, Integer> idToIndex = new HashMap<Integer, Integer>();
    private List<T> cache = new ArrayList<T>();
    
    private boolean loaded = false;
    private final EventBus eventBus;

    protected AbstractModelCache(SessionManager<?> manager, EventBus eventBus){
        manager.addCache(this);
        this.eventBus = eventBus;
    }

    public void purge(){
        loaded = false;
        idToIndex.clear();
        cache.clear();
    }
    
    protected void addAll(List<T> models){ 
        idToIndex.clear();
        int index = 0;
        for(T model: models){
            idToIndex.put(model.getId(), index++);
        }
        this.cache = models;
    }

    protected void add(T model){
        idToIndex.put(model.getId(), cache.size());        
        cache.add(model);
    }

    protected void replace(T model){
        cache.set(idToIndex.get(model.getId()), model);
    }
    
    protected void remove(T model){
        cache.set(idToIndex.remove(model.getId()), null);
    }
    
    protected void onModelEvent(ModelEvent<T> event) {
        switch(event.getAction()){
            case LOAD:
                if (event.getModels() != null){
                    addAll(event.getModels());
                    loaded = true;
                }
                else if (event.getModel() != null){
                    if(idToIndex.containsKey(event.getModel().getId())) {
                        replace(event.getModel());
                    }
                    else{
                        add(event.getModel());
                    }
                }
                break;
            case UPDATE:
                replace(event.getModel());
                break;
            case CREATE:
                add(event.getModel());
                break;
            case DESTROY:
                remove(event.getModel());
        }     
    }

    abstract protected GwtEvent<?> newEvent(List<T> response);
    
    protected MethodCallback<List<T>> newMethodCallback() {
        return new MethodCallback<List<T>>() {

            public void onSuccess(Method method, List<T> response) {
                eventBus.fireEvent(newEvent(response));
            }

            public void onFailure(Method method, Throwable exception) {
                eventBus.fireEvent(newEvent(null));
            }
        };
    }

    public List<T> getOrLoadModels(){
        if (!loaded){
            loadModels();
            return null;
        }
        List<T> result = new ArrayList<T>();
        for(T item: cache){
            if(item != null){
                result.add(item);
            }
        }
        return result;
    }

    abstract protected void loadModels();
    abstract protected T newModel();

    public T getModel(int id) {
        if(idToIndex.containsKey(id)){
            return cache.get(idToIndex.get(id));
        }
        return newModel();
    }
}