package <%= managed_package %>;

import javax.inject.Inject;
<% if options[:session] -%>

import <%= models_package %>.User;
<% end -%>

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;

import <%= gwt_rails_package %>.places.RestfulActionEnum;
<% if options[:session] -%>
import <%= gwt_rails_package %>.session.SessionManager;
<% end -%>
import <%= gwt_rails_package %>.views.MenuPanel;

@Singleton
public class ToolsMenuPanel extends MenuPanel<User> {

    @Inject
    ToolsMenuPanel(final PlaceController placeController<% if options[:session] -%>, SessionManager<User> sessionManager<% end %>){
        super(<% if options[:session] -%>sessionManager<% end -%>);
    }
}