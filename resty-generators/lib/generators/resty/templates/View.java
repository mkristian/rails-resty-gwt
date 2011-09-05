package <%= views_package %>;

<% unless options[:singleton] -%>
import java.util.List;

<% end -%>
import <%= models_package %>.<%= class_name %>;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import <%= gwt_rails_package %>.places.RestfulAction;

@ImplementedBy(<%= class_name %>ViewImpl.class)
public interface <%= class_name %>View extends IsWidget {

    public interface Presenter {
<% unless options[:singleton] -%>
        void create();
<% end -%>        
        void save();
<% unless options[:singleton] -%>
        void delete(<%= class_name %> model);
<% end -%>
        void goTo(Place place);
    }
    void setPresenter(Presenter presenter);

    void reset(<%= class_name %> model);
<% unless options[:singleton] -%>

    void reset(List<<%= class_name %>> models);
<% end -%>

    void reset(RestfulAction action);
    
    void setEnabled(boolean enabled);

    <%= class_name %> retrieve<%= class_name %>();
<% unless options[:singleton] -%>

    void updateInList(<%= class_name %> model);

    void removeFromList(<%= class_name %> model);

    void addToList(<%= class_name %> model);
<% end -%>
}