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

    protected final CRUDView<T> view;
    protected final CRUDListView<T> listView;
    protected final Cache<T> cache;
    protected final Remote<T> remote;
    private boolean isEditing = false;
    private T model;

    public CRUDPresenterImpl(ErrorHandlerWithDisplay errors,
                CRUDView<T> view,
                CRUDListView<T> listView,
                Cache<T> cache,
                Remote<T> remote) {
        super(errors);
        this.view = view;
        this.view.setPresenter(this);
        this.listView = listView;
        this.listView.setPresenter(this);
        this.cache = cache;
        this.remote = remote;
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
    public void edit(int id) {
        edit( cache.getOrLoadModel(id) );
    }

    @Override
    public void edit(T model) {
        this.model = model;
        isEditing = true;
        setWidget( view );
        view.edit( model );
    }

    @Override
    public void showNew() {
        isEditing = true;
        setWidget( view );
        view.showNew();
    }

    @Override
    public void show(T model) {
        this.model = model;
        isEditing = false;
        setWidget( view );
        view.show( model );
    }

    @Override
    public void show(int id) {
        show( cache.getOrLoadModel( id ) );
    }

    public void create(final T model) {
        this.model = model;
        remote.create( model );
    }

    public void delete(final T model) {
        this.model = model;
        remote.delete( model );
    }

    @Override
    public void reset(T model) {
        this.model = model;
        view.reset( model );
    }

    @Override
    public void reset(List<T> models) {
        listView.reset( models );
    }

    @Override
    public void save(T model) { 
        this.model = model;
        remote.update( model );
    }

    @Override
    public boolean isDirty() {
        return isEditing && view.isDirty();
    }

    @Override
    public void reload() {
        remote.retrieve( model.getId() );
    }

}