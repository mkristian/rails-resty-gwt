package <%= places_package %>;

import <%= gwt_rails_package %>.RestfulAction;

import <%= managed_package %>.ActivityFactory;
import <%= base_package %>.ActivityPlace;

import com.google.gwt.activity.shared.Activity;

public class <%= class_name %>Place extends ActivityPlace {
    
    public Activity create(ActivityFactory factory){
        return factory.create(this);
    }
    
    public <%= class_name %>Place(RestfulAction restfulAction) {
        super(restfulAction, "<%= table_name %>");
    }

    public <%= class_name %>Place(int id, RestfulAction restfulAction) {
        super(id, restfulAction, "<%= table_name %>");
    }    
    
    public <%= class_name %>Place(String id, RestfulAction restfulAction) {
        super(id, restfulAction, "<%= table_name %>");
    }
}