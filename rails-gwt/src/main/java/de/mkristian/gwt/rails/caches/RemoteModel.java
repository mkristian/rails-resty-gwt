package de.mkristian.gwt.rails.caches;

import java.util.List;

import org.fusesource.restygwt.client.Method;

import de.mkristian.gwt.rails.models.Identifyable;

public interface RemoteModel<T extends Identifyable> {

    void fireCreate(Method method, T model);

    void fireRetrieve(Method method, List<T> models);

    void fireRetrieve(Method method, T model);

    void fireUpdate(Method method, T model);

    void fireDelete(Method method, T model);

    void fireError(Method method, Throwable e);

    T newModel();
    
    void create(T model);
    
    void retrieve(int id);
    
    void retrieveAll();
    
    void update(T model);
    
    void delete(T model);
}