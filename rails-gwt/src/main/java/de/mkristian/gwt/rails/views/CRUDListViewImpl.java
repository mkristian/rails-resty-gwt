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

import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.places.RestfulAction;
import de.mkristian.gwt.rails.places.RestfulActionEnum;
import de.mkristian.gwt.rails.presenters.CRUDPresenter;
import de.mkristian.gwt.rails.session.Guard;

public abstract class CRUDListViewImpl<T extends Identifyable> extends Composite {

    @UiField(provided = true) public Label header;
    @UiField public FlexTable list;

    private CRUDPresenter<T> presenter;
    
    private final ClickHandler clickHandler = newClickHandler();
    
    protected final PlaceController places;
    protected final Guard guard;

    public CRUDListViewImpl( String title, Guard guard, PlaceController places ) {
        this.header = new Label( title );
        this.places = places;
        this.guard = guard;
    }

    protected boolean isAllowed( RestfulActionEnum action ){
        return guard.isAllowed( placeName(), action );
    }
    
    protected abstract String placeName();

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
                    case DESTROY:
                        presenter.delete(button.model);
                        break;
                    case EDIT:
                    case SHOW:
                        places.goTo( place( button.model, button.action ) );
                        break;
                    default:
                        presenter.unknownAction( button.action );
                }
            }
        };
    }
    
    public void remove(T model) {
        String id = model.getId() + "";
        for(int i = 0; i < list.getRowCount(); i++){
            if(list.getText(i, 0).equals(id)){
                list.removeRow(i);
                return;
            }
        }
    }

    public void setPresenter(CRUDPresenter<T> presenter) {
        this.presenter = presenter;
    }

}