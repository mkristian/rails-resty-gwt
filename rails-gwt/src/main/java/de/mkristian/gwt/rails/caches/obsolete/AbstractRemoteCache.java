package de.mkristian.gwt.rails.caches.obsolete;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.caches.Cache;
import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEvent.Action;
import de.mkristian.gwt.rails.models.Identifyable;

public abstract class AbstractRemoteCache<T extends Identifyable> implements Cache<T> {
    
    protected final EventBus eventBus;

    protected AbstractRemoteCache(EventBus eventBus){
        this.eventBus = eventBus;
    }
    
    protected void onLoad(Method method, List<T> models){
        if (models != null){
            replaceAll(models);
        }
        eventBus.fireEvent(newEvent(models, Action.LOAD));
    }
    
    protected void onLoad(Method method, T model){
        addOrReplace(model);
        eventBus.fireEvent(newEvent(model, Action.LOAD));
    }
    
    public void onError(Method method, Throwable e){
        eventBus.fireEvent(newEvent(e));
    }
    
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
    
    abstract protected void addOrReplace(T model);
    abstract protected void replaceAll(List<T> models);

    abstract protected ModelEvent<T> newEvent(List<T> models, Action action);
    abstract protected ModelEvent<T> newEvent(T model, Action action);
    abstract protected ModelEvent<T> newEvent(Throwable e);
}