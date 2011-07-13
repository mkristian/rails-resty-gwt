package <%= views_package %>;

import java.util.List;

import <%= models_package %>.<%= class_name %>;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import <%= gwt_rails_package %>.RestfulAction;

@ImplementedBy(<%= class_name %>ViewImpl.class)
public interface <%= class_name %>View extends IsWidget {

    public interface Presenter {
<% unless options[:singleton] -%>
        void create();
<% end -%>        
        void save();
<% unless options[:singleton] -%>
        void delete();
<% end -%>
        void goTo(Place place);
    }
    void setPresenter(Presenter presenter);

    void reset(<%= class_name %> model);

    void reset(RestfulAction action);
    
    void setEnabled(boolean enabled);

    <%= class_name %> retrieve<%= class_name %>();
}