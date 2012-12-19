package de.mkristian.gwt.rails.views;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.presenters.CRUDPresenter;


public interface CRUDListView<T extends Identifyable> extends IsWidget {
    void setPresenter( CRUDPresenter<T> presenter );

    void reset( List<T> model );

  //  void add( T model );

    void remove( T model );
    }