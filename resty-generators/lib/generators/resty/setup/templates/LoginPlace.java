package <%= places_package %>;

import <%= managed_package %>.ActivityFactory;

import <%= gwt_rails_package %>.places.RestfulPlace;

import com.google.gwt.activity.shared.Activity;

public class LoginPlace extends RestfulPlace<Void, ActivityFactory> {

    public static final LoginPlace LOGIN = new LoginPlace();

    private LoginPlace() {
        super(null, null);
    }

    @Override
    public Activity create(ActivityFactory factory) {
        return factory.create(this);
    }
}
