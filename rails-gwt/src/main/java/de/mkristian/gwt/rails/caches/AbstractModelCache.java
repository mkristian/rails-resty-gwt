package de.mkristian.gwt.rails.caches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEvent.Action;
import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.session.SessionManager;

public abstract class AbstractModelCache<T extends Identifyable> implements Cache {
    
    private final Map<Integer, Integer> idToIndex = new HashMap<Integer, Integer>();
    private List<T> cache = new ArrayList<T>();
    
    private boolean loaded = false;
    protected final EventBus eventBus;

    protected AbstractModelCache(EventBus eventBus){
        this(null, eventBus);
    }
     
    //TODO replace SessionMAnager wth CacheManager interface
    protected AbstractModelCache(SessionManager<?> manager, EventBus eventBus){
        if (manager != null){
            manager.addCache(this);
        }
        this.eventBus = eventBus;
    }

    public void purge(){
        loaded = false;
        idToIndex.clear();
        cache.clear();
    }

    public void purge(T model){
        remove(model);
    }
    
    protected void addAll(List<T> models){ 
        idToIndex.clear();
        int index = 0;
        for(T model: models){
            idToIndex.put(model.getId(), index++);
        }
        this.cache = new ArrayList<T>(models);
    }

    protected void add(T model){
        idToIndex.put(model.getId(), cache.size());        
        cache.add(model);
    }

    protected void put(T model){
        cache.set(idToIndex.get(model.getId()), model);
    }
    
    protected T get(int id){
        if (idToIndex.containsKey(id)) {
            return cache.get(idToIndex.get(id));
        }
        else {
            return null;
        }
    }
    
    protected void remove(T model){
        cache.set(idToIndex.remove(model.getId()), null);
    }

    private void doPut(T model){
        if (model != null){
            if (idToIndex.containsKey(model.getId())) {
                put(model);
            }
            else {
                add(model);
            }
        }
    }

    protected void onLoad(Method method, List<T> models){
        if (models != null){
            addAll(models);
        }
        loaded = true;
        eventBus.fireEvent(newEvent(models, Action.LOAD));
    }
    
    protected void onLoad(Method method, T model){
        doPut(model);
        eventBus.fireEvent(newEvent(model, Action.LOAD));
    }
    
    public void onError(Method method, Throwable e){
        eventBus.fireEvent(newEvent(e));
    }

    public void onCreate(Method method, T model){
        add(model);
        eventBus.fireEvent(newEvent(model, Action.CREATE));
    }
    
    public void onUpdate(Method method, T model){
        doPut(model);
        eventBus.fireEvent(newEvent(model, Action.UPDATE));
    }
    
    public void onDestroy(Method method, T model){
        remove(model);
        eventBus.fireEvent(newEvent(model, Action.DESTROY));
    }
    
//    protected void onModelEvent(ModelEvent<T> event) {
//        switch(event.getAction()){
//            case LOAD:
//                if (event.getModels() != null){
//                    addAll(event.getModels(), event.getRawData());
//                    loaded = true;
//                }
//                else if (event.getModel() != null){
//                    if(idToIndex.containsKey(event.getModel().getId())) {
//                        replace(event.getModel(), event.getRawData());
//                    }
//                    else{
//                        add(event.getModel(), event.getRawData());
//                    }
//                }
//                break;
//            case UPDATE:
//                replace(event.getModel(), event.getRawData());
//                break;
//            case CREATE:
//                add(event.getModel(), event.getRawData());
//                break;
//            case DESTROY:
//                remove(event.getModel(), event.getRawData());
//        }
//        eventBus.fireEvent(event);
//    }

    abstract protected ModelEvent<T> newEvent(List<T> models, Action action);
    abstract protected ModelEvent<T> newEvent(T model, Action action);
    abstract protected ModelEvent<T> newEvent(Throwable e);
    
    protected MethodCallback<List<T>> newListMethodCallback() {
        return new MethodCallback<List<T>>() {

            public void onSuccess(Method method, List<T> response) {
                onLoad(method, response);
            }

            public void onFailure(Method method, Throwable exception) {
                // TODO maybe propagate the exception or do nothing
                onError(method, exception);
            }
        };
    }

    protected MethodCallback<T> newMethodCallback() {
        return new MethodCallback<T>() {

            public void onSuccess(Method method, T response) {
                onLoad(method, response);
            }

            public void onFailure(Method method, Throwable exception) {
                // TODO maybe propagate the exception or do nothing
                onError(method, exception);
            }
        };
    }
    
    public T getOrLoadModel(int id){
        T model = get(id);
        if (model == null){
            loadModel(id);
            model = newModel();
        }
        return model;
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
    abstract protected void loadModel(int id);
    abstract protected T newModel();

    protected T getModel(int id) {
        if(idToIndex.containsKey(id)){
            return cache.get(idToIndex.get(id));
        }
        return newModel();
    }
}