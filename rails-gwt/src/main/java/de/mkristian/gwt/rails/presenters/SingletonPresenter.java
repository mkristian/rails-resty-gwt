package de.mkristian.gwt.rails.presenters;

public interface SingletonPresenter<T>
            extends Presenter {

    void reload();

    void save( T model );

    void reset( T model );
    
    void show();
    void show( T model );

    void edit();
    void edit( T model );

    boolean isDirty();

    T get();

}