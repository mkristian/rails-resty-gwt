package <%= base_package %>;

import javax.inject.Inject;

import <%= managed_package %>.ActivityFactory;
import <%= models_package %>.User;
import <%= places_package %>.LoginPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

import <%= gwt_rails_package %>.Notice;
import <%= gwt_rails_package %>.places.RestfulPlace;
import <%= gwt_rails_package %>.session.NeedsAuthorization;
import <%= gwt_rails_package %>.session.NoAuthorization;
import <%= gwt_rails_package %>.session.SessionManager;

public class SessionActivityPlaceActivityMapper extends ActivityPlaceActivityMapper {

    private final SessionManager<User> manager;

    @Inject
    public SessionActivityPlaceActivityMapper(ActivityFactory factory, SessionManager<User> manager, Notice notice) {
        super(factory, notice);
        this.manager = manager;
    }

    public Activity getActivity(Place place) {
        return pessimisticGetActivity(place);
    }

    /**
     * pessimistic in the sense that default is authorisation, only the places
     * which implements {@link NoAuthorization} will be omitted by the check.
     */
    protected Activity pessimisticGetActivity(Place place) {
        if (!(place instanceof NoAuthorization)) {
            if(manager.hasSession()){
                if(!manager.isAllowed((RestfulPlace<?>)place)){
                    notice.setText("nothing to see");
                    return null;
                }
                //TODO move into a dispatch filter or callback filter
                manager.resetTimer();
            }
            else {
                return LoginPlace.LOGIN.create(factory);
            }
        }
        return super.getActivity(place);
    }

    /**
     * optimistic in the sense that default is no authorisation, only the places
     * which implements {@link NeedsAuthorization} will be checked.
     */
    protected Activity optimisticGetActivity(Place place) {
        if (place instanceof NeedsAuthorization) {
            if(manager.hasSession()){
                if(!manager.isAllowed((RestfulPlace<?>)place)){
                    notice.setText("nothing to see");
                    return null;
                }
            }
            else {
                return LoginPlace.LOGIN.create(factory);
            }
        }
        return super.getActivity(place);
    }
}
