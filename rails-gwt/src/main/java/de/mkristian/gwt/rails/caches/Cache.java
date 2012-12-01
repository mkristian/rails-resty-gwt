package de.mkristian.gwt.rails.caches;

import java.util.List;

import de.mkristian.gwt.rails.models.Identifyable;

public interface Cache<T extends Identifyable> {

    void purgeAll();
    
    void remove(T model);

    void removeAll();
    
    T getModel(int id);
    
    T getOrLoadModel(int id);
    
    List<T> getModels();

    List<T> getOrLoadModels();
}