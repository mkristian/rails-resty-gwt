package <%= views_package %>;

<% if !options[:singleton] || attributes.detect { |a| a.type == :belongs_to} -%>
import java.util.List;

<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= models_package %>.<%= attribute.name.classify %>;
<% end -%>
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

    void edit(<%= class_name %> model);

    <%= class_name %> flush();
<% unless options[:singleton] -%>

    void reset(List<<%= class_name %>> models);
<% end -%>

    void reset(RestfulAction action);
    
    void setEnabled(boolean enabled);
<% unless options[:singleton] -%>

    void updateInList(<%= class_name %> model);

    void removeFromList(<%= class_name %> model);

    void addToList(<%= class_name %> model);
<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>

    void reset<%= attribute.name.classify.to_s.pluralize %>(List<<%= attribute.name.classify %>> list);
<% end -%>
<% end -%>
}