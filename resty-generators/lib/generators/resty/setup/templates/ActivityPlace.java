package <%= managed_package %>;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

public abstract class ActivityPlace extends Place {

    public abstract Activity create(ActivityFactory factory);
}