package de.mkristian.gwt.rails.views;

import com.google.gwt.user.client.ui.IsWidget;

import de.mkristian.gwt.rails.presenters.SingletonPresenter;


public interface SingletonView<T> extends IsWidget {

    void setPresenter( SingletonPresenter<T> presenter );

    void show( T model );

    void edit( T model );

    boolean isDirty();
}