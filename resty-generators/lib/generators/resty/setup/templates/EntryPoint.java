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

import de.mkristian.gwt.rails.Application;
import de.mkristian.gwt.rails.Notice;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class <%= application_name %> implements EntryPoint {

    @GinModules(<%= application_name %>Module.class)
    static public interface <%= application_name %>Ginjector extends Ginjector {
	PlaceController getPlaceController();
	EventBus getEventBus();        
	Application getApplication();
    }
    
    static public class <%= application_name %>Application extends Application {
	private final Notice notice;
        
	@Inject
	<%= application_name %>Application(final Notice notice, 
					   final ActivityManager activityManager){
	    super(activityManager);
	    this.notice = notice;
	}
        
	protected Panel getApplicationPanel(){
	    Panel root = RootPanel.get("<%= application_name.underscore %>");
	    root.add(notice);
	    return root;
	}
    }
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
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