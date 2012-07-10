/**
 *
 */
package de.mkristian.gwt.rails.events;


import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

public abstract class ModelEvent<T> extends GwtEvent<ModelEventHandler<T>> {

    public static enum Action { LOAD, CREATE, UPDATE, DESTROY, ERROR }

    private final Throwable throwable;
    
    private final T model;

    private final ModelEvent.Action action;

    private final List<T> models;

    public ModelEvent(Throwable throwable) {
        this(null, null, throwable, Action.ERROR);
    }
    
    public ModelEvent(final T model, ModelEvent.Action action) {
        this(model, null, null, action);
    }

    public ModelEvent(final List<T> models, ModelEvent.Action action) {
        this(null, models, null, action);
    }

    private ModelEvent(final T model, final List<T> models, Throwable throwable, ModelEvent.Action action) {
        this.model = model;
        this.models = models;
        this.action = action;
        this.throwable = throwable;
    }

    public T getModel() {
        return model;
    }
    
    public List<T> getModels() {
        return models;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public ModelEvent.Action getAction() {
        return action;
    }

    public String toString() {
        if( model == null){
            return getClass().getName() + "#" + models + "#" + action;
        }
        else {
            return getClass().getName() + "#" + model + "#" + action;
        }
    }

    @Override
    protected void dispatch(ModelEventHandler<T> handler) {
        handler.onModelEvent(this);
    }
}