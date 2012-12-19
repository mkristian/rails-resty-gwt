package de.mkristian.gwt.rails.presenters;

import de.mkristian.gwt.rails.ErrorHandlerWithDisplay;
import de.mkristian.gwt.rails.caches.RemoteSingleton;
import de.mkristian.gwt.rails.views.SingletonView;

public abstract class SingletonPresenterImpl<T> 
            extends AbstractPresenter
            implements SingletonPresenter<T> {

    protected final RemoteSingleton<T> remote;
    protected final SingletonView<T> view;
    
    protected boolean isEditing = false;
    private T model;

    public SingletonPresenterImpl( ErrorHandlerWithDisplay errors,
                SingletonView<T> view,
                RemoteSingleton<T> remote) {
        super(errors);
        this.view = view;
        this.view.setPresenter( this );
        this.remote = remote;
    }

    public T get() {
        return model;
    }
    
    public void save( final T model ) {
        this.model = model;
        isEditing = false;
        remote.update( model );
    }

    public void show(){
        isEditing = false;
        setWidget( view );
        remote.retrieve();
    }

    public void edit(){
        isEditing = true;
        setWidget( view );
        remote.retrieve();
    }

    public void reset( T model ) {
        this.model = model;
        if( isEditing ) {
            view.edit( model );
        }
        else {
            view.show( model );
        }
    }

    public void reload() {
        if( isEditing ) {
            edit();
        }
        else {
            show();
        }
    }

    public void show( T model ) {
        this.model = model;
        isEditing = false;
        setWidget( view );
        view.show( model );
    }

    public void edit( T model ) {
        this.model = model;
        isEditing = true;
        setWidget( view );
        view.edit( model );
    }

    public boolean isDirty() {
        return isEditing && view.isDirty();
    }

}