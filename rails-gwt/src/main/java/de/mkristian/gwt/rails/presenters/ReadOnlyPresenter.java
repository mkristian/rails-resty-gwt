package de.mkristian.gwt.rails.presenters;

import java.util.List;

import de.mkristian.gwt.rails.models.Identifiable;

public interface ReadOnlyPresenter<T extends Identifiable> 
            extends Presenter {

    void reset( T model );

    void reset( List<T> models );

    void showAll();

    void show( int id );

}