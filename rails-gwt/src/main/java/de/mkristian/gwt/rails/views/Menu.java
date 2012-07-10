package de.mkristian.gwt.rails.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import de.mkristian.gwt.rails.places.RestfulAction;
import de.mkristian.gwt.rails.places.RestfulPlace;

public class Menu extends FlowPanel {

    public static class PlaceButton extends Button {
        private final RestfulPlace<?, ?> place;
        private final RestfulAction action;

        public PlaceButton(String name, RestfulPlace<?, ?> place, RestfulAction action){
            super(name);
            this.place = place;
            this.action = action;
        }
        
        public RestfulPlace<?, ?> getPlace(){
            return place;
        }
    }

    private final PlaceController placeController;
    
    protected Menu(PlaceController placeController){
        setStyleName("gwt-rails-menu");
        this.placeController = placeController;
    }

    protected Button addButton(String name) {
        return addButton(name, null, null);
    }
    
    protected Button addButton(String name, final RestfulPlace<?, ?> place) {
        return addButton(name, place, place.action);
    }
    
    protected Button addButton(String name, final RestfulPlace<?, ?> place, RestfulAction action) {
        return addButton(new PlaceButton(name, place, action));
    }
    
    protected Button addButton(final PlaceButton button) {
        add(button);
        if (button.place != null){
            button.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    placeController.goTo(button.getPlace());
                }
            });
        }
        return button;
    }
}