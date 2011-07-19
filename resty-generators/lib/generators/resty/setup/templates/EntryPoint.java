package <%= base_package %>;

import <%= managed_package %>.<%= application_name %>PlaceHistoryMapper;
import <%= managed_package %>.<%= application_name %>Module;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

import <%= gwt_rails_package %>.Application;
import <%= gwt_rails_package %>.Notice;
import <%= gwt_rails_package %>.DefaultDispatcherSingleton;

import org.fusesource.restygwt.client.Defaults;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class <%= application_name %>EntryPoint implements EntryPoint {

    @GinModules(<%= application_name %>Module.class)
    static public interface <%= application_name %>Ginjector extends Ginjector {
        PlaceController getPlaceController();
        EventBus getEventBus();
        Application getApplication();
    }

    static public class <%= application_name %>Application extends Application {
        private final Notice notice;
<% if options[:session] -%>
        private final BreadCrumbsPanel breadCrumbs;
<% end -%>
        private RootPanel root;

        @Inject
        <%= application_name %>Application(final Notice notice,
<% if options[:session] -%>
                                           final BreadCrumbsPanel breadCrumbs,
<% end -%>
                                           final ActivityManager activityManager){
            super(activityManager);
            this.notice = notice;
<% if options[:session] -%>
            this.breadCrumbs = breadCrumbs;
<% end -%>
 }

        protected Panel getApplicationPanel(){
            if (this.root == null) {
                this.root = RootPanel.get();
                this.root.add(notice);
<% if options[:session] -%>
                this.root.add(breadCrumbs);
<% end -%>
            }
            return this.root;
        }
    }

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Defaults.setServiceRoot(GWT.getModuleBaseURL().replaceFirst("[a-z]+/$", ""));
        Defaults.setDispatcher(DefaultDispatcherSingleton.INSTANCE);
        GWT.log("base url for restservices: " + Defaults.getServiceRoot());

        final <%= application_name %>Ginjector injector = GWT.create(<%= application_name %>Ginjector.class);

        injector.getApplication().run();

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        <%= application_name %>PlaceHistoryMapper historyMapper = GWT.create(<%= application_name %>PlaceHistoryMapper.class);

        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(injector.getPlaceController(), injector.getEventBus(), Place.NOWHERE);

        // Goes to the place represented on URL else default place
        historyHandler.handleCurrentHistory();
    }
}