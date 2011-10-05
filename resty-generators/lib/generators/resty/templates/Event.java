package <%= events_package %>;
<% unless options[:singleton] -%>

import java.util.List;
<% end -%>

import <%= models_package %>.<%= class_name %>;

import <%= gwt_rails_package %>.events.ModelEvent;
import <%= gwt_rails_package %>.events.ModelEventHandler;

public class <%= class_name %>Event extends ModelEvent<<%= class_name %>> {

    public static final Type<ModelEventHandler<<%= class_name %>>> TYPE = new Type<ModelEventHandler<<%= class_name %>>>();
    
    public <%= class_name %>Event(<%= class_name %> model, ModelEvent.Action action) {
        super(model, action);
    }
<% unless options[:singleton] -%>

    public <%= class_name %>Event(List<<%= class_name %>> models, ModelEvent.Action action) {
        super(models, action);
    }
<% end -%>

    @Override
    public Type<ModelEventHandler<<%= class_name %>>> getAssociatedType() {
        return TYPE;
    }
}