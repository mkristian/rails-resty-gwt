package <%= managed_package %>;

import com.google.gwt.activity.shared.Activity;
import com.google.inject.name.Named;
<% if options[:session] -%>
import <%= places_package %>.LoginPlace;
<% end -%>
public interface ActivityFactory {
<% if options[:session] -%>
    @Named("login") Activity create(LoginPlace place);
<% end -%>
}