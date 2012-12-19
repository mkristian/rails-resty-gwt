package de.mkristian.gwt.rails.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

import de.mkristian.gwt.rails.places.RestfulAction;
import de.mkristian.gwt.rails.places.RestfulActionEnum;

public abstract class ReadOnlyListViewImpl<T> extends Composite {

    @UiField(provided = true) public Label header;
    @UiField public FlexTable list;
    
    private final ClickHandler clickHandler = newClickHandler();
    
    protected final PlaceController places;

    public ReadOnlyListViewImpl( String title, PlaceController places ) {
        this.header = new Label( title );
        this.places = places;
    }

    protected abstract Place place( T model, RestfulAction action );
    
    protected Button newButton(RestfulActionEnum action, T model) {
        ModelButton<T> button = new ModelButton<T>(action, model);
        button.addClickHandler(clickHandler);
        return button;
    }

    protected ClickHandler newClickHandler(){
        return new ClickHandler() {
            @SuppressWarnings("unchecked")
            public void onClick(ClickEvent event) {
                ModelButton<T> button = (ModelButton<T>)event.getSource();
                switch(button.action){
                    case SHOW:
                        places.goTo( place( button.model, button.action ) );
                        break;
                    default:
                       // presenter.unknownAction( button.action.name() );
                }
            }
        };
    }
}