package de.mkristian.gwt.rails.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEventHandler;
import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.places.RestfulActionEnum;
import de.mkristian.gwt.rails.places.RestfulPlace;
import de.mkristian.gwt.rails.presenters.CRUDPresenter;

public abstract class AbstractCRUDActivitiy<T extends Identifyable> extends AbstractActivity {

    protected final RestfulPlace<T, ?> place;
    protected final CRUDPresenter<T> presenter;
    protected final PlaceController places;

    public AbstractCRUDActivitiy( RestfulPlace<T, ?> place, 
            CRUDPresenter<T> presenter,
            PlaceController places ) {
        this.place = place;
        this.presenter = presenter;
        this.places = places;
    }
 
    abstract protected Type<ModelEventHandler<T>> eventType();

    abstract protected RestfulPlace<T, ?> showPlace( T model );
    
    abstract protected RestfulPlace<T, ?> showAllPlace();
    
   // @Override
    public void start(AcceptsOneWidget display, EventBus eventBus) {
        presenter.setDisplay(display);
        eventBus.addHandler(eventType(), new ModelEventHandler<T>(){
           // @Override
            public void onModelEvent(ModelEvent<T> event) {
                switch(event.getAction()){
                    case LOAD:
                        if (event.getModel() != null) {
                            presenter.reset( event.getModel() );
                        }
                        if (event.getModels() != null) {
                            presenter.reset( event.getModels() );
                        }
                        break;
                    case UPDATE:
                    case CREATE:
                        places.goTo( showPlace( event.getModel() ) );
                        break;
                    case DESTROY:
                        places.goTo( showAllPlace() );
                        break;
                    case ERROR:
                        presenter.onError( event.getMethod(), 
                                    event.getThrowable() );
                        break;
                    default:
                        presenter.unknownAction( event.getAction() );
                        break;
                }
            }
    
        });
        switch(RestfulActionEnum.valueOf(place.action)){
            case SHOW:
                if ( place.model != null ) {
                    presenter.show( place.model );
                }
                else {
                    presenter.show( place.id );
                }
                break;
            case NEW:
                presenter.showNew();
                break;
            case EDIT:
                if ( place.model != null ) {
                    presenter.edit( place.model );
                }
                else {
                    presenter.edit( place.id );
                }
                break;
            case INDEX:
                presenter.showAll();
                break;
            default:
                presenter.unknownAction( place.action );
                break;
        }
    }

    @Override
    public String mayStop() {
        if (presenter.isDirty()){
            return "there are unsaved data.";
        }
        else {
            return null;
        }
    }

}