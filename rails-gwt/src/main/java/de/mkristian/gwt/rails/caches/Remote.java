package de.mkristian.gwt.rails.caches;

import org.fusesource.restygwt.client.Method;

import de.mkristian.gwt.rails.models.Identifyable;

public interface Remote<T extends Identifyable> extends RemoteReadOnly<T> {

    void fireCreate(Method method, T model);

    void fireUpdate(Method method, T model);

    void fireDelete(Method method, T model);

    T newModel();
    
    void create(T model);
    
    void update(T model);
    
    void delete(T model);
}