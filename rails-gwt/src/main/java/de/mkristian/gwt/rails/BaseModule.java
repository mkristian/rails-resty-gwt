/**
 * 
 */
package de.mkristian.gwt.rails;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class BaseModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(PlaceController.class).toProvider(
                PlaceControllerProvider.class).in(Singleton.class);
        bind(ActivityManager.class).toProvider(
                ActivityManagerProvider.class).in(Singleton.class);
    }

    public static class PlaceControllerProvider implements Provider<PlaceController> {

        private final EventBus eventBus;

        @Inject
        public PlaceControllerProvider(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        public PlaceController get() {
            return new PlaceController(eventBus);
        }
    }

    public static class ActivityManagerProvider implements Provider<ActivityManager> {

        private final EventBus eventBus;
        private final ActivityMapper activityMapper;

        @Inject
        public ActivityManagerProvider(ActivityMapper activityMapper, EventBus eventBus) {
            this.eventBus = eventBus;
            this.activityMapper = activityMapper;
        }

        public ActivityManager get() {
            return new ActivityManager(activityMapper, eventBus);
        }
    }
    
//    static class PlaceHistoryHandlerProvider implements Provider<PlaceHistoryHandler> {
//
//        private final PlaceHistoryMapper mapper;
//
//        @Inject
//        public PlaceHistoryHandlerProvider(PlaceHistoryMapper mapper) {
//            this.mapper = mapper;
//        }
//
//        public PlaceHistoryHandler get() {
//            return new PlaceHistoryHandler(mapper);
//        }
//    }

}