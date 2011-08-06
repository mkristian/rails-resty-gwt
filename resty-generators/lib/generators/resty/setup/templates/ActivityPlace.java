package <%= base_package %>;

import <%= managed_package %>.ActivityFactory;

import com.google.gwt.activity.shared.Activity;

import <%= gwt_rails_package %>.RestfulAction;
import <%= gwt_rails_package %>.RestfulPlace;

public abstract class ActivityPlace extends RestfulPlace {

    protected ActivityPlace(RestfulAction restfulAction, String name) {
        super(restfulAction, name);
    }

    protected ActivityPlace(int id, RestfulAction restfulAction, String name) {
        super(id, restfulAction, name);
    }

    protected ActivityPlace(String id, RestfulAction restfulAction, String name) {
        super(id, restfulAction, name);
    }

    public abstract Activity create(ActivityFactory factory);
}