package de.mkristian.gwt.rails.caches;

import java.util.List;

import org.fusesource.restygwt.client.Method;

import de.mkristian.gwt.rails.models.Identifyable;

public interface RemoteReadOnly<T extends Identifyable> {

    void fireRetrieve(Method method, List<T> models);

    void fireRetrieve(Method method, T model);

    void fireError(Method method, Throwable e);
    
    void retrieve(int id);
    
    void retrieveAll();
}