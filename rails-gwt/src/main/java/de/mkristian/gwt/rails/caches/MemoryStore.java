package de.mkristian.gwt.rails.caches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mkristian.gwt.rails.models.Identifiable;

public class MemoryStore<T extends Identifiable> implements Store<T> {
    
    private final Map<Integer, Integer> idToIndex = new HashMap<Integer, Integer>();
    private List<T> cache = new ArrayList<T>();
    
    private boolean loaded = false;
    
    public void removeAll(){
        loaded = false;
        idToIndex.clear();
        cache.clear();
    }
    
    public void purgeAll(){
        removeAll();
    }
    
    public void replaceAll(List<T> models, String json){ 
        if( models != null ){
            loaded = true;
            idToIndex.clear();
            int index = 0;
            for(T model: models){
                idToIndex.put(model.getId(), index++);
            }
            this.cache = new ArrayList<T>(models);
        }
    }
    
    public T get(int id){
        if (idToIndex.containsKey(id)) {
            return cache.get(idToIndex.get(id));
        }
        else {
            return null;
        }
    }

    public List<T> getAll(){
        if ( loaded ){
            List<T> result = new ArrayList<T>();
            for(T item: cache){
                if(item != null){
                    result.add(item);
                }
            }
            return result;
        }
        else {
            return null;
        }
    }

    public void remove(T model){
        cache.set(idToIndex.remove(model.getId()), null);
    }

    public void update(T model, String json){
        if (model != null){
            if (idToIndex.containsKey(model.getId())) {
                cache.set(idToIndex.get(model.getId()), model);
            }
            else {
                idToIndex.put(model.getId(), cache.size());        
                cache.add(model);
            }
        }
    }
}