package de.mkristian.gwt.rails.caches;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEvent.Action;
import de.mkristian.gwt.rails.models.Identifyable;

public abstract class AbstractRemoteModel<T extends Identifyable> implements RemoteModel<T> {

    protected final EventBus eventBus;

    protected AbstractRemoteModel(EventBus eventBus){
        this.eventBus = eventBus;
    }
    
    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.caches.RemoteModel#fireCreate(org.fusesource.restygwt.client.Method, T)
     */
    public void fireCreate(Method method, T model){
        eventBus.fireEvent(newEvent(method, model, Action.CREATE));
    }
    
    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.caches.RemoteModel#fireRetrieve(org.fusesource.restygwt.client.Method, java.util.List)
     */
    public void fireRetrieve(Method method, List<T> models){
        eventBus.fireEvent(newEvent(method, models, Action.LOAD));
    }
    
    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.caches.RemoteModel#fireRetrieve(org.fusesource.restygwt.client.Method, T)
     */
    public void fireRetrieve(Method method, T model){
        eventBus.fireEvent(newEvent(method, model, Action.LOAD));
    }
    
    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.caches.RemoteModel#fireUpdate(org.fusesource.restygwt.client.Method, T)
     */
    public void fireUpdate(Method method, T model){
        eventBus.fireEvent(newEvent(method, model, Action.UPDATE));
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.caches.RemoteModel#fireDelete(org.fusesource.restygwt.client.Method, T)
     */
    public void fireDelete(Method method, T model){
        eventBus.fireEvent(newEvent(method, model, Action.DESTROY));
    }
    
    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.caches.RemoteModel#fireError(org.fusesource.restygwt.client.Method, java.lang.Throwable)
     */
    public void fireError(Method method, Throwable e){
        eventBus.fireEvent(newEvent(method, e));
    }

    protected MethodCallback<T> newCreateCallback() {
        return new MethodCallback<T>() {

            public void onSuccess(Method method, T response) {
                fireCreate(method, response);
            }

            public void onFailure(Method method, Throwable exception) {
                // TODO maybe propagate the exception or do nothing
                fireError(method, exception);
            }
        };
    }
    
    protected MethodCallback<T> newRetrieveCallback() {
        return new MethodCallback<T>() {

            public void onSuccess(Method method, T response) {
                fireRetrieve(method, response);
            }

            public void onFailure(Method method, Throwable exception) {
                // TODO maybe propagate the exception or do nothing
                fireError(method, exception);
            }
        };
    }

    protected MethodCallback<List<T>> newRetrieveAllCallback() {
        return new MethodCallback<List<T>>() {

            public void onSuccess(Method method, List<T> response) {
                fireRetrieve(method, response);
            }

            public void onFailure(Method method, Throwable exception) {
                // TODO maybe propagate the exception or do nothing
                fireError(method, exception);
            }
        };
    }
    
    protected MethodCallback<T> newUpdateCallback() {
        return new MethodCallback<T>() {

            public void onSuccess(Method method, T response) {
                fireUpdate(method, response);
            }

            public void onFailure(Method method, Throwable exception) {
                // TODO maybe propagate the exception or do nothing
                fireError(method, exception);
            }
        };
    }
    
    protected MethodCallback<T> newDeleteCallback() {
        return new MethodCallback<T>() {

            public void onSuccess(Method method, T response) {
                fireDelete(method, response);
            }

            public void onFailure(Method method, Throwable exception) {
                // TODO maybe propagate the exception or do nothing
                fireError(method, exception);
            }
        };
    }

    abstract protected ModelEvent<T> newEvent(Method method, List<T> models, Action action);
    abstract protected ModelEvent<T> newEvent(Method method, T model, Action action);
    abstract protected ModelEvent<T> newEvent(Method method, Throwable e);
}