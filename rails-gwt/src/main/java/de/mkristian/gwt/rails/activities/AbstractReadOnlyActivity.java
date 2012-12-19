package de.mkristian.gwt.rails.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.events.ModelEventHandler;
import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.places.RestfulActionEnum;
import de.mkristian.gwt.rails.places.RestfulPlace;
import de.mkristian.gwt.rails.presenters.ReadOnlyPresenter;

public abstract class AbstractReadOnlyActivity<T extends Identifyable> 
            extends AbstractActivity {

    protected final RestfulPlace<T, ?> place;
    protected final ReadOnlyPresenter<T> presenter;

    public AbstractReadOnlyActivity( RestfulPlace<T, ?> place,
            ReadOnlyPresenter<T> presenter ) {   
        this.place = place;
        this.presenter = presenter;
    }
    
    protected abstract Type<ModelEventHandler<T>> eventType();

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
                        if (event.getModels() != null) {
                            presenter.reset( event.getModels() );
                        }
                        break;
                    default:
                        presenter.unknownAction( event.getAction() );
                        break;
                }
            }
        });
        switch( RestfulActionEnum.valueOf( place.action ) ){
            case SHOW:
                presenter.show( place.id );
                break;
            case INDEX:
                presenter.showAll();
                break;
            default:
                presenter.unknownAction( place.action );
                break;
        }
    }

}