package <%= managed_package %>;

import <%= base_package %>.<%= application_name %>.<%= application_name %>Application;

import <%= gwt_rails_package %>.Application;
import <%= gwt_rails_package %>.BaseModule;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class <%= application_name %>Module extends BaseModule {

    @Override
    protected void configure() {   
        super.configure();
        bind(Application.class).to(<%= application_name %>Application.class);
	bind(ActivityMapper.class).to(ActivityPlaceActivityMapper.class).in(Singleton.class);
	install(new GinFactoryModuleBuilder()
            .build(ActivityFactory.class));  
    }
}