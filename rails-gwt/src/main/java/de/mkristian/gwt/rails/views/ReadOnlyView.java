package de.mkristian.gwt.rails.views;

import com.google.gwt.user.client.ui.IsWidget;

public interface ReadOnlyView<T> extends IsWidget {
    
    void show(T model);
}