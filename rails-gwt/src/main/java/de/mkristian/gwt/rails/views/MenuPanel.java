/**
 *
 */
package de.mkristian.gwt.rails.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import de.mkristian.gwt.rails.places.RestfulAction;
import de.mkristian.gwt.rails.places.RestfulPlace;
import de.mkristian.gwt.rails.session.SessionHandler;
import de.mkristian.gwt.rails.session.SessionManager;

public class MenuPanel<T> extends FlowPanel {

    static class PlaceButton extends Button {
        private final RestfulPlace<?, ?> place;
        private final RestfulAction action;

        PlaceButton(String name, RestfulPlace<?, ?> place, RestfulAction action){
            super(name);
            this.place = place;
            this.action = action;
        }
    }

    private PlaceController placeController;
    
    protected MenuPanel(){
        setStyleName("gwt-rails-menu");
        setVisible(true);
    }
    protected MenuPanel(final SessionManager<T> sessionManager){
        this(sessionManager, null);
    }
    
    protected MenuPanel(final SessionManager<T> sessionManager, PlaceController placeController){
        this(); 
        this.placeController = placeController;
        setVisible(false);
        sessionManager.addSessionHandler(new SessionHandler<T>() {

            public void timeout() {
                setVisible(false);
            }

            public void logout() {
                setVisible(false);
            }

            public void login(T user) {
                for(int i = 0; i < getWidgetCount(); i++){
                    PlaceButton b = (PlaceButton)getWidget(i);
                    b.setVisible(b.place == null ? 
                            true : 
                            sessionManager.isAllowed(b.place.resourceName, b.action));
                }
                setVisible(true);
            }

            public void accessDenied() {
            }
        });
    }

    protected Button addButton(String name) {
        return addButton(name, null, null);
    }
    
    protected Button addButton(String name, final RestfulPlace<?, ?> place) {
        return addButton(name, place, place.action);
    }
    
    protected Button addButton(String name, final RestfulPlace<?, ?> place, RestfulAction action) {
        Button button = new PlaceButton(name, place, action);
        add(button);
        if (place != null){
            button.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    placeController.goTo(place);
                }
            });
        }
        return button;
    }
}