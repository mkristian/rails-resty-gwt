package de.mkristian.gwt.rails.caches;

import java.util.List;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.session.CacheManager;

public abstract class AbstractPreemptiveCache<T extends Identifyable> extends AbstractCache<T> {
    
    protected AbstractPreemptiveCache(EventBus eventBus, 
            Store<T> store, RemoteModel<T> remote){
        this(eventBus, store, remote, null);
    }
    
    protected AbstractPreemptiveCache(EventBus eventBus,  
            Store<T> store, RemoteModel<T> remote, 
            CacheManager manager){
        super(eventBus, store, remote, manager);
    }
    
    public T getOrLoadModel(int id){
        T model = getModel(id);
        remote.retrieve(id);
        if (model == null){
            model = remote.newModel();
        }
        return model;
    }
    
    public List<T> getOrLoadModels(){
        List<T> result = getModels();
        remote.retrieveAll();
        return result;
    }
}