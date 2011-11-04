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
<% unless options[:read_only] -%>
<% unless options[:singleton] -%>
        void create();
<% end -%>
        void save();
<% unless options[:singleton] -%>
        void delete(<%= class_name %> model);
<% end -%>
<% end -%>
        void goTo(Place place);
    }

    void setup(Presenter presenter, RestfulAction action);
<% unless options[:singleton] -%>

    void reset(List<<%= class_name %>> models);
<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>

    void reset<%= attribute.name.classify.to_s.pluralize %>(List<<%= attribute.name.classify %>> list);
<% end -%>
<% end -%>

    void edit(<%= class_name %> model);
<% unless options[:read_only] -%>

    <%= class_name %> flush();
<% end -%>
<% if !options[:singleton] && !options[:read_only] -%>

    void removeFromList(<%= class_name %> model);
<% end -%>
}