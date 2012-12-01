package de.mkristian.gwt.rails.views;

import com.google.gwt.place.shared.PlaceController;

import de.mkristian.gwt.rails.session.Guard;

public class SessionMenu extends Menu {

    private final Guard guard;
    
    protected SessionMenu(PlaceController placeController, final Guard guard){
        super(placeController);
        this.guard = guard;
        setStyleName("gwt-rails-menu");
        setVisible(false);
    }
     
    @Override
    public void setupButtons(boolean visible) {
        if (visible){
            for(PlaceButton button: buttons){
                if (guard.isAllowed(button.place.resourceName, button.action)) {
                    add(button);
                }
            }
        }
        else {
            clear();
        }
    }
}