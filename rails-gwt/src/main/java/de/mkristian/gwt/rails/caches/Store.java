package de.mkristian.gwt.rails.caches;

import java.util.List;

import de.mkristian.gwt.rails.models.Identifyable;

public interface Store<T extends Identifyable> {

    void update(T model, String json);

    void replaceAll(List<T> models, String json);

    T get(int id);

    List<T> getAll();

    void remove(T model);

    void removeAll();
    
    void purgeAll();

}