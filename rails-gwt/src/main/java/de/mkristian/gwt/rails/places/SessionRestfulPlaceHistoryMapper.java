package de.mkristian.gwt.rails.places;

import com.google.gwt.place.shared.Place;

import de.mkristian.gwt.rails.session.HasSession;

public class SessionRestfulPlaceHistoryMapper extends RestfulPlaceHistoryMapper {

    private final HasSession session;
    
    protected SessionRestfulPlaceHistoryMapper(HasSession session){
        this.session = session;
    }
    
    @Override
    public Place getPlace(String token) {
        RestfulPlace<?, ?> place = (RestfulPlace<?, ?>) super.getPlace(token);
        if (place != null) {
            // place needs to be different on the level of equals in order to trigger an activity
            place.hasSession = session.hasSession();
        }
        return place;
    }

}