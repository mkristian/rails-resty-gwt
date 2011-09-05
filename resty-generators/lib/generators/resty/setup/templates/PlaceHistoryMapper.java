package <%= managed_package %>;

import javax.inject.Inject;
import javax.inject.Singleton;

<% if options[:session] -%>
import <%= models_package %>.User;

import com.google.gwt.place.shared.Place;

import <%= gwt_rails_package %>.places.RestfulPlace;
<% end -%>
import <%= gwt_rails_package %>.places.RestfulPlaceHistoryMapper;
<% if options[:session] -%>
import <%= gwt_rails_package %>.session.SessionManager;
<% end -%>

@Singleton
public class <%= application_name %>PlaceHistoryMapper extends RestfulPlaceHistoryMapper {
<% if options[:session] -%>

    private final SessionManager<User> manager;
<% end -%>

    @Inject
    public <%= application_name %>PlaceHistoryMapper(<% if options[:session] -%>SessionManager<User> manager<% end -%>){
<% if options[:session] -%>
        this.manager = manager;
<% end -%>
    }
<% if options[:session] -%>

    @Override
    public Place getPlace(String token) {
        RestfulPlace<?> place = (RestfulPlace<?>) super.getPlace(token);
        // place needs to be different on the level of equals in order to trigger an activity
        place.hasSession = manager.hasSession(); 
        return place;
    }
<% end -%>
}
