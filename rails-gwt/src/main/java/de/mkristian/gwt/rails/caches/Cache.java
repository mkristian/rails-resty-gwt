package de.mkristian.gwt.rails.caches;

import java.util.List;

import de.mkristian.gwt.rails.models.Identifiable;

public interface Cache<T extends Identifiable> {

    void purgeAll();
    
    void remove(T model);

    void removeAll();
    
    T getModel(int id);
    
    T getOrLoadModel(int id);
    
    List<T> getModels();

    List<T> getOrLoadModels();
}