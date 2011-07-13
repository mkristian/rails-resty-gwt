package <%= managed_package %>;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

import <%= gwt_rails_package %>.RestfulAction;
import <%= gwt_rails_package %>.RestfulPlace;

public abstract class ActivityPlace extends RestfulPlace {

    protected ActivityPlace(RestfulAction restfulAction) {
        super(restfulAction);
    }

    protected ActivityPlace(int id, RestfulAction restfulAction) {
        super(id, restfulAction);
    }

    protected ActivityPlace(String id, RestfulAction restfulAction) {
        super(id, restfulAction);
    }

    public abstract Activity create(ActivityFactory factory);
}