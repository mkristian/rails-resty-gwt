package de.mkristian.gwt.rails.presenters;

import java.util.List;

import de.mkristian.gwt.rails.models.Identifiable;



public interface CRUDPresenter<T extends Identifiable> extends Presenter {

    void reload();
    
    void create( T model );

    void save( T model );

    void delete( T model );

    void reset( T model );
    void reset( List<T> models );

    void showNew();
    void showAll();
    void show( int id );
    void show( T model );

    void edit( int id );
    void edit( T model );
    
    boolean isDirty();

    T get();
}