package de.mkristian.gwt.rails.caches;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.RemoteNotifier;
import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEvent.Action;
import de.mkristian.gwt.rails.models.Identifyable;

public abstract class AbstractRemote<T extends Identifyable> 
            implements Remote<T> {

    protected final EventBus eventBus;
    protected final RemoteNotifier notifier;
    
    protected AbstractRemote(EventBus eventBus,
                    RemoteNotifier notifier){
        this.eventBus = eventBus;
        this.notifier = notifier;
    }
    
    public void fireCreate(Method method, T model){
        notifier.finish();
        eventBus.fireEvent(newEvent(method, model, Action.CREATE));
    }

    public void fireRetrieve(Method method, List<T> models){
        notifier.finish();
        eventBus.fireEvent(newEvent(method, models, Action.LOAD));
    }

    public void fireRetrieve(Method method, T model){
        notifier.finish();
        eventBus.fireEvent(newEvent(method, model, Action.LOAD));
    }

    public void fireUpdate(Method method, T model){
        notifier.finish();
        eventBus.fireEvent(newEvent(method, model, Action.UPDATE));
    }

    public void fireDelete(Method method, T model){
        notifier.finish();
        eventBus.fireEvent(newEvent(method, model, Action.DESTROY));
    }

    public void fireError(Method method, Throwable e){
        notifier.finish();
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
    
    protected MethodCallback<Void> newDeleteCallback( final T model ) {
        return new MethodCallback<Void>() {

            public void onSuccess( Method method, Void response ) {
                fireDelete( method, model );
            }

            public void onFailure( Method method, Throwable exception ) {
                // TODO maybe propagate the exception or do nothing
                fireError(method, exception);
            }
        };
    }

    abstract protected ModelEvent<T> newEvent(Method method, List<T> models, Action action);
    abstract protected ModelEvent<T> newEvent(Method method, T model, Action action);
    abstract protected ModelEvent<T> newEvent(Method method, Throwable e);
}