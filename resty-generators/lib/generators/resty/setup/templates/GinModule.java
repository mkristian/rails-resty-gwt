package <%= managed_package %>;

import <%= base_package %>.<%= application_name %>EntryPoint.<%= application_name %>Application;
import <%= base_package %>.<% if options[:session] -%>Session<% end -%>ActivityPlaceActivityMapper;
<% if options[:session] -%>
import <%= activities_package %>.LoginActivity;
<% end -%>
import <%= gwt_rails_package %>.Application;
import <%= gwt_rails_package %>.BaseModule;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

<% if options[:session] -%>
import <%= views_package %>.LoginViewImpl;

import <%= gwt_rails_session_package %>.LoginView;
<% end -%>
public class <%= application_name %>Module extends BaseModule {

    @Override
    protected void configure() {
        super.configure();
        bind(Application.class).to(<%= application_name %>Application.class);
        bind(PlaceHistoryMapper.class).to(<%= application_name %>PlaceHistoryMapper.class).in(Singleton.class);
        bind(ActivityMapper.class).to(<% if options[:session] -%>Session<% end -%>ActivityPlaceActivityMapper.class).in(Singleton.class);
<% if options[:session] -%>
        bind(LoginView.class).to(LoginViewImpl.class);
<% end -%>
        install(new GinFactoryModuleBuilder()
<% if options[:session] -%>
		.implement(Activity.class, Names.named("login"), LoginActivity.class)
<% end -%>
            .build(ActivityFactory.class));
    }
}