package de.mkristian.gwt.rails.views;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface ReadOnlyListView<T> extends IsWidget {
    void reset(List<T> models);
}