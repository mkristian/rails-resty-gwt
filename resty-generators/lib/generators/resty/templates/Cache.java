package <%= caches_package %>;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import <%= events_package %>.<%= class_name %>Event;
import <%= events_package %>.<%= class_name %>EventHandler;
import <%= models_package %>.<%= class_name %>;
import <%= models_package %>.User;
import <%= restservices_package %>.<%= class_name.pluralize %>RestService;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;

import <%= gwt_rails_package %>.caches.AbstractModelCache;
import <%= gwt_rails_package %>.events.ModelEvent;
import <%= gwt_rails_package %>.events.ModelEvent.Action;
import <%= gwt_rails_package %>.session.SessionManager;

@Singleton
public class <%= class_name.pluralize %>Cache extends AbstractModelCache<<%= class_name %>>{

    private final <%= class_name.pluralize %>RestService restService;
    
    @Inject
    <%= class_name.pluralize %>Cache(SessionManager<User> manager, EventBus eventBus, <%= class_name.pluralize %>RestService restService) {
        super(manager, eventBus);
        this.restService = restService;
        eventBus.addHandler(<%= class_name %>Event.TYPE, new <%= class_name %>EventHandler() {
            
            public void onModelEvent(ModelEvent<<%= class_name %>> event) {
                <%= class_name.pluralize %>Cache.this.onModelEvent(event);
            }
        });
    }
    
    protected void loadModels() {
        restService.index(newMethodCallback());
    }

    protected <%= class_name %> newModel() {
        return new <%= class_name %>();
    }

    @Override
    protected GwtEvent<?> newEvent(List<<%= class_name %>> response) {
        return new <%= class_name %>Event(response, Action.LOAD);
    }
}