package de.mkristian.gwt.rails.caches;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.RemoteNotifier;

public abstract class RemoteSingletonAdapter<T> 
        extends AbstractRemoteSingleton<T>{

    protected RemoteSingletonAdapter( EventBus eventBus,
                    RemoteNotifier notifier ) {
        super( eventBus, notifier );
    }

    public void retrieve() {
        throw new RuntimeException( "not implemented" );
    }

    public void update(T model) {
        throw new RuntimeException( "not implemented" );
    }
}