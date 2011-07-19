package <%= base_package %>;

import <%= managed_package %>.ActivityFactory;

import com.google.gwt.activity.shared.Activity;

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