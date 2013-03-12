package de.mkristian.gwt.rails.presenters;

import java.util.List;

import de.mkristian.gwt.rails.ErrorHandlerWithDisplay;
import de.mkristian.gwt.rails.caches.Cache;
import de.mkristian.gwt.rails.caches.Remote;
import de.mkristian.gwt.rails.models.Identifiable;
import de.mkristian.gwt.rails.views.CRUDListView;
import de.mkristian.gwt.rails.views.CRUDView;

public class CRUDPresenterImpl<T extends Identifiable>
        extends AbstractPresenter
        implements CRUDPresenter<T> {

    private final CRUDView<T, ?> view;
    private final CRUDListView<T> listView;
    private final Cache<T> cache;
    private final Remote<T> remote;
    private boolean isEditing = false;
    private T model;

    public CRUDPresenterImpl(ErrorHandlerWithDisplay errors,
                CRUDView<T, ?> view,
                CRUDListView<T> listView,
                Cache<T> cache,
                Remote<T> remote) {
        super(errors);
        this.view = view;
        this.listView = listView;
        this.listView.setPresenter(this);
        this.cache = cache;
        this.remote = remote;
    }

    public Remote<T> getRemote() {
        return remote;
    }

    protected CRUDView<T, ?> getView() {
        return view;
    }

    public T get() {
        return model;
    }

    @Override
    public void showAll() {
        isEditing = false;
        setWidget( listView );
        reset( cache.getOrLoadModels() );
    }

    @Override
    public void edit( int id ) {
        doEdit( cache.getOrLoadModel( id ) );
    }

    private void doEdit( T model ) {
        this.model = model;
        isEditing = true;
        setWidget( getView() );
        view.edit( model );
    }

    @Override
    public void edit( T model ) {
        T m = cache.getOrLoadModel( model.getId() );
        doEdit( m == null ? model : m );
    }
    
    @Override
    public void showNew() {
        isEditing = true;
        setWidget( getView() );
        view.showNew();
    }

    @Override
    public void show(T model) {
        T m = cache.getOrLoadModel( model.getId() );
        doShow( m == null ? model : m );
    }

    private void doShow(T model) {
        this.model = model;
        isEditing = false;
        setWidget( getView() );
        view.show( model );
    }

    @Override
    public void show(int id) {
        doShow( cache.getOrLoadModel( id ) );
    }

    public void create(final T model) {
        this.model = model;
        isEditing = false;
        getRemote().create( model );
    }

    public void delete(final T model) {
        this.model = model;
        getRemote().delete( model );
    }

    @Override
    public void reset(T model) {
        this.model = model;
        getView().reset( model );
    }

    @Override
    public void reset(List<T> models) {
        listView.reset( models );
    }

    @Override
    public void save(T model) { 
        this.model = model;
        isEditing = false;
        getRemote().update( model );
    }

    @Override
    public boolean isDirty() {
        return isEditing && getView().isDirty();
    }

    @Override
    public void reload() {
        getRemote().retrieve( model.getId() );
    }

}