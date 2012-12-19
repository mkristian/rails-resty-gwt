package de.mkristian.gwt.rails.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEventHandler;
import de.mkristian.gwt.rails.places.RestfulActionEnum;
import de.mkristian.gwt.rails.places.RestfulPlace;
import de.mkristian.gwt.rails.presenters.SingletonPresenter;

public abstract class AbstractSingletonActivity<T> extends AbstractActivity {

    protected final RestfulPlace<T, ?> place;
    protected final SingletonPresenter<T> presenter;
    protected final PlaceController places;

    public AbstractSingletonActivity( RestfulPlace<T, ?> place,
            SingletonPresenter<T> presenter,
            PlaceController places  ) {
        this.place = place;
        this.presenter = presenter;
        this.places = places;
    }

    protected abstract Type<ModelEventHandler<T>> eventType();

    protected abstract RestfulPlace<T, ?> showPlace( T model );
        
    public void start(AcceptsOneWidget display, EventBus eventBus) {
        presenter.setDisplay( display );
        eventBus.addHandler(eventType(), new ModelEventHandler<T>(){
            //@Override
            public void onModelEvent(ModelEvent<T> event) {
                switch(event.getAction()){
                    case LOAD:
                        if (event.getModel() != null) {
                            presenter.reset( event.getModel() );
                        }
                        break;
                    case UPDATE:
                        places.goTo( showPlace( event.getModel() ) );
                        break;
                    case ERROR:
                        presenter.onError( event.getMethod(), event.getThrowable());
                        break;
                    default:
                        presenter.unknownAction( event.getAction() );
                        break;
                }
            }
    
        });
        switch( RestfulActionEnum.valueOf( place.action ) ){
            case EDIT:
                if ( place.model != null ){
                    presenter.edit( place.model );
                }
                else {
                    presenter.edit();
                }
                break;
            case SHOW:
                if ( place.model != null ){
                    presenter.show( place.model );
                }
                else {
                    presenter.show();
                }
                break;
            default:
                presenter.unknownAction( place.action );
                break;
        }
    }

    @Override
    public String mayStop() {
        if (presenter.isDirty()){
            return "there is unsaved data.";
        }
        else {
            return null;
        }
    }

}