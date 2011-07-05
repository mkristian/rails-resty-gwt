package <%= managed_package %>;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

public class ActivityPlaceActivityMapper implements ActivityMapper {
    private final ActivityFactory factory;

    @Inject
    public ActivityPlaceActivityMapper(ActivityFactory factory) {
        this.factory = factory;
    }
    
    
    public Activity getActivity(Place place) {
        if (place instanceof ActivityPlace) {
            return ((ActivityPlace) place).create(factory);
        }
        return null;
    }
}