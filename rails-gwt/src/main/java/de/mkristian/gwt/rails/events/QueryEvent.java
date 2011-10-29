package de.mkristian.gwt.rails.events;

import com.google.gwt.event.shared.GwtEvent;

public class QueryEvent extends GwtEvent<QueryEventHandler> {

    private static final Type<QueryEventHandler> TYPE = new Type<QueryEventHandler>();
    
    @Override
    public Type<QueryEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(QueryEventHandler handler) {
        handler.onQueryEvent(new QueryEvent());
    }

    public static Type<QueryEventHandler> getType() {
        return TYPE;
    }
}