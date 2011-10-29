package <%= places_package %>;

import <%= gwt_rails_package %>.places.RestfulAction;
import <%= gwt_rails_package %>.places.RestfulPlace;

import <%= managed_package %>.ActivityFactory;
import <%= models_package %>.<%= class_name %>;

import com.google.gwt.activity.shared.Activity;

public class <%= class_name %>Place extends RestfulPlace<<%= class_name %>, ActivityFactory> {
    
    public static final String NAME = "<%= table_name %>";

    public Activity create(ActivityFactory factory){
        return factory.create(this);
    }
    
    public <%= class_name %>Place(RestfulAction restfulAction) {
        super(restfulAction, NAME);
    }

    public <%= class_name %>Place(<%= class_name %> model, RestfulAction restfulAction) {
        super(<% unless options[:singleton] -%>model.getId(), <% end -%>model, restfulAction, NAME);
    }

    public <%= class_name %>Place(int id, RestfulAction restfulAction) {
        super(id, restfulAction, NAME);
    }    
    
    public <%= class_name %>Place(String id, RestfulAction restfulAction) {
        super(id, restfulAction, NAME);
    }
}