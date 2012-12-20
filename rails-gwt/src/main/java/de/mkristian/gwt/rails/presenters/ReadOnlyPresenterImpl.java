package de.mkristian.gwt.rails.presenters;

import java.util.List;

import de.mkristian.gwt.rails.ErrorHandlerWithDisplay;
import de.mkristian.gwt.rails.caches.RemoteReadOnly;
import de.mkristian.gwt.rails.models.Identifiable;
import de.mkristian.gwt.rails.views.ReadOnlyListView;
import de.mkristian.gwt.rails.views.ReadOnlyView;

public class ReadOnlyPresenterImpl<T extends Identifiable> 
            extends AbstractPresenter
            implements ReadOnlyPresenter<T> {

    protected final ReadOnlyView<T> view;
    protected final ReadOnlyListView<T> listView;
    protected final RemoteReadOnly<T> remote;

    public ReadOnlyPresenterImpl( ErrorHandlerWithDisplay errors,
            ReadOnlyView<T> view, 
            ReadOnlyListView<T> listView, 
            RemoteReadOnly<T> remote ) {
        super(errors);
        this.view = view;
        this.listView = listView;
        this.remote = remote;
    }

    public void reset( T model ) {
        view.show( model );
    }

    public void reset( List<T> models ) {
        listView.reset( models );
    }

    public void showAll() {
        setWidget(listView);
        remote.retrieveAll();
    }

    public void show( int id ) {
        setWidget(view);
        remote.retrieve( id );
    }

}