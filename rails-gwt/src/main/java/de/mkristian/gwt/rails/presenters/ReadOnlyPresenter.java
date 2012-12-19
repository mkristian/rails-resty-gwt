package de.mkristian.gwt.rails.presenters;

import java.util.List;

import de.mkristian.gwt.rails.models.Identifyable;

public interface ReadOnlyPresenter<T extends Identifyable> 
            extends Presenter {

    void reset( T model );

    void reset( List<T> models );

    void showAll();

    void show( int id );

}