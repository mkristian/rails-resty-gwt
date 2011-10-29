package de.mkristian.gwt.rails.events;

import com.google.gwt.event.shared.EventHandler;

public interface QueryEventHandler extends EventHandler {
    void onQueryEvent(QueryEvent event);
}