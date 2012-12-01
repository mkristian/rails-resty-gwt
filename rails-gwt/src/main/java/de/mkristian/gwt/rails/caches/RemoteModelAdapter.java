package de.mkristian.gwt.rails.caches;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.models.Identifyable;

public abstract class RemoteModelAdapter<T extends Identifyable> extends AbstractRemoteModel<T>{

    protected RemoteModelAdapter(EventBus eventBus) {
        super(eventBus);
    }

    public T newModel() {
        throw new RuntimeException( "not implemented" );
    }

    public void create(T model) {
        throw new RuntimeException( "not implemented" );
    }

    public void retrieve(int id) {
        throw new RuntimeException( "not implemented" );
    }

    public void retrieveAll() {
        throw new RuntimeException( "not implemented" );
    }

    public void update(T model) {
        throw new RuntimeException( "not implemented" );
    }

    public void delete(T model) {
        throw new RuntimeException( "not implemented" );
    }        
}