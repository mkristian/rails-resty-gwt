package de.mkristian.ixtlan.gwt.domains;

import java.util.List;

import org.fusesource.restygwt.client.Method;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEventHandler;

public class DomainEvent extends ModelEvent<Domain> {

    public static final Type<ModelEventHandler<Domain>> TYPE = new Type<ModelEventHandler<Domain>>();
    
    public DomainEvent( Method method, Domain model,
                ModelEvent.Action action ) {
        super( method, model, action );
    }

    public DomainEvent( Method method, List<Domain> models,
                ModelEvent.Action action ) {
        super( method, models, action );
    }

    public DomainEvent( Method method, Throwable e) {
        super( method, e );
    }

    @Override
    public Type<ModelEventHandler<Domain>> getAssociatedType() {
        return TYPE;
    }
}