package de.mkristian.gwt.rails;

import org.fusesource.restygwt.client.callback.XSRFToken;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.mkristian.gwt.rails.dispatchers.DispatcherFactory;

public class BaseGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(XSRFToken.class).toProvider(XSRFTokenProvider.class).in(Singleton.class);
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    }

    public static class XSRFTokenProvider implements Provider<XSRFToken> {

        public XSRFToken get() {
            return DispatcherFactory.INSTANCE.xsrf;
        }
    }
}