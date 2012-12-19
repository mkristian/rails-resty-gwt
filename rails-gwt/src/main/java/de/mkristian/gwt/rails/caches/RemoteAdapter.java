package de.mkristian.gwt.rails.caches;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.RemoteNotifier;
import de.mkristian.gwt.rails.models.Identifyable;

public abstract class RemoteAdapter<T extends Identifyable> 
            extends AbstractRemote<T>{

    protected RemoteAdapter( EventBus eventBus,
                    RemoteNotifier notifier ) {
        super( eventBus, notifier );
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