package <%= base_package %>;

import <%= managed_package %>.ActivityFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

import de.mkristian.gwt.rails.Notice;

public class ActivityPlaceActivityMapper implements ActivityMapper {
    protected final ActivityFactory factory;
    protected final Notice notice;

    @Inject
    public ActivityPlaceActivityMapper(ActivityFactory factory, Notice notice) {
        this.notice = notice;
        this.factory = factory;
    }


    public Activity getActivity(Place place) {
        if (place instanceof ActivityPlace) {
            return ((ActivityPlace) place).create(factory);
        }
        notice.setText("nothing to see");
        return null;
    }
}