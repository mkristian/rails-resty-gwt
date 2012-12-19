package de.mkristian.gwt.rails.caches;

import org.fusesource.restygwt.client.Method;

public interface RemoteSingleton<T> {

    void fireRetrieve(Method method, T model);

    void fireUpdate(Method method, T model);

    void fireError(Method method, Throwable e);
    
    void retrieve();
    
    void update(T model);
}