/**
 *
 */
package de.mkristian.gwt.rails.events;


import com.google.gwt.event.shared.EventHandler;

public interface ModelEventHandler<T> extends EventHandler {
    void onModelEvent(ModelEvent<T> event);
}