package de.mkristian.gwt.rails.caches;

import com.google.gwt.event.shared.EventBus;

import de.mkristian.gwt.rails.RemoteNotifier;
import de.mkristian.gwt.rails.models.Identifyable;

public abstract class RemoteReadOnlyAdapter<T extends Identifyable> 
            extends AbstractRemoteReadOnly<T>{

    protected RemoteReadOnlyAdapter( EventBus eventBus,
                    RemoteNotifier notifier ) {
        super( eventBus, notifier );
    }

    public void retrieve(int id) {
        throw new RuntimeException( "not implemented" );
    }

    public void retrieveAll() {
        throw new RuntimeException( "not implemented" );
    }
}