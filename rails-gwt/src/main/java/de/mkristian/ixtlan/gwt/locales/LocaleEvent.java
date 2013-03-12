package de.mkristian.ixtlan.gwt.locales;

import java.util.List;

import org.fusesource.restygwt.client.Method;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEventHandler;

public class LocaleEvent extends ModelEvent<Locale> {

    public static final Type<ModelEventHandler<Locale>> TYPE = new Type<ModelEventHandler<Locale>>();
    
    public LocaleEvent( Method method, Locale model,
                ModelEvent.Action action ) {
        super( method, model, action );
    }

    public LocaleEvent( Method method, List<Locale> models,
                ModelEvent.Action action ) {
        super( method, models, action );
    }

    public LocaleEvent( Method method, Throwable e) {
        super( method, e );
    }

    @Override
    public Type<ModelEventHandler<Locale>> getAssociatedType() {
        return TYPE;
    }
}