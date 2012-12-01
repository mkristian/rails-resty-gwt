package de.mkristian.gwt.rails.dispatchers;

import org.fusesource.restygwt.client.cache.DefaultQueueableCacheStorage;
import org.fusesource.restygwt.client.cache.QueueableCacheStorage;
import org.fusesource.restygwt.client.callback.CachingCallbackFilter;
import org.fusesource.restygwt.client.callback.CallbackFactory;
import org.fusesource.restygwt.client.callback.CallbackFilter;
import org.fusesource.restygwt.client.callback.DefaultCallbackFactory;
import org.fusesource.restygwt.client.callback.RestfulCachingCallbackFilter;
import org.fusesource.restygwt.client.callback.RetryingCallbackFactory;
import org.fusesource.restygwt.client.callback.XSRFTokenCallbackFilter;
import org.fusesource.restygwt.client.dispatcher.CachingDispatcherFilter;
import org.fusesource.restygwt.client.dispatcher.DefaultDispatcherFilter;
import org.fusesource.restygwt.client.dispatcher.DefaultFilterawareDispatcher;
import org.fusesource.restygwt.client.dispatcher.DispatcherFilter;
import org.fusesource.restygwt.client.dispatcher.FilterawareDispatcher;
import org.fusesource.restygwt.client.dispatcher.RestfulCachingDispatcherFilter;
import org.fusesource.restygwt.client.dispatcher.XSRFTokenDispatcherFilter;

import com.google.gwt.core.client.GWT;

public class DispatcherFactory {

    static class XSRFToken extends org.fusesource.restygwt.client.callback.XSRFToken {
        public void setToken(String token){
            if(getToken() == null){
                super.setToken(token);
            }
        }
        
        public void flush(){
            super.setToken(null);
        }

    }
    public static DispatcherFactory INSTANCE = new DispatcherFactory();

    public final XSRFToken xsrf = new XSRFToken();
    public final QueueableCacheStorage cache = new DefaultQueueableCacheStorage();
    
    private DispatcherFactory(){
    }

    public FilterawareDispatcher xsrfProtectionDispatcher(){
        DispatcherFilter xsrfDispatcherFilter = new XSRFTokenDispatcherFilter(xsrf);

        CallbackFilter xsrfCallbackFilter = new XSRFTokenCallbackFilter(xsrf);
        CallbackFactory callbackFactory = new DefaultCallbackFactory(xsrfCallbackFilter);
        DispatcherFilter defaultDispatcherFilter = new DefaultDispatcherFilter(callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(xsrfDispatcherFilter, defaultDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher cachingDispatcher(){
        CallbackFilter cachingCallbackFilter = new CachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new DefaultCallbackFactory(cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new CachingDispatcherFilter(cache,callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher cachingXSRFProtectionDispatcher(){
        DispatcherFilter xsrfDispatcherFilter = new XSRFTokenDispatcherFilter(xsrf);

        CallbackFilter xsrfCallbackFilter = new XSRFTokenCallbackFilter(xsrf);

        CallbackFilter cachingCallbackFilter = new CachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new DefaultCallbackFactory(xsrfCallbackFilter, cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new CachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(xsrfDispatcherFilter, cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher retryingDispatcher(){
        CallbackFilter cachingCallbackFilter = new CachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new RetryingCallbackFactory(cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new CachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher retryingCachingDispatcher(){
        CallbackFilter cachingCallbackFilter = new CachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new RetryingCallbackFactory(cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new CachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher retryingCachingXSRFProtectionDispatcher(){
        DispatcherFilter xsrfDispatcherFilter = new XSRFTokenDispatcherFilter(xsrf);

        CallbackFilter xsrfCallbackFilter = new XSRFTokenCallbackFilter(xsrf);

        CallbackFilter cachingCallbackFilter = new CachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new RetryingCallbackFactory(xsrfCallbackFilter, cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new CachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(xsrfDispatcherFilter, cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher restfulCachingDispatcher(){
        CallbackFilter cachingCallbackFilter = new RestfulCachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new DefaultCallbackFactory(cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new RestfulCachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher restfulCachingXSRFProtectionDispatcher(){
        DispatcherFilter xsrfDispatcherFilter = new XSRFTokenDispatcherFilter(xsrf);

        CallbackFilter xsrfCallbackFilter = new XSRFTokenCallbackFilter(xsrf);

        CallbackFilter cachingCallbackFilter = new RestfulCachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new DefaultCallbackFactory(xsrfCallbackFilter, cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new RestfulCachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(xsrfDispatcherFilter, cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher restfulRetryingCachingDispatcher(){
        CallbackFilter cachingCallbackFilter = new RestfulCachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new RetryingCallbackFactory(cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new RestfulCachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(cachingDispatcherFilter);

        return dispatcher;
    }

    public FilterawareDispatcher restfulRetryingCachingXSRFProtectionDispatcher(){
        DispatcherFilter xsrfDispatcherFilter = new XSRFTokenDispatcherFilter(xsrf);

        CallbackFilter xsrfCallbackFilter = new XSRFTokenCallbackFilter(xsrf);

        CallbackFilter cachingCallbackFilter = new RestfulCachingCallbackFilter(cache);
        CallbackFactory callbackFactory = new RetryingCallbackFactory(xsrfCallbackFilter, cachingCallbackFilter);
        DispatcherFilter cachingDispatcherFilter = new RestfulCachingDispatcherFilter(cache, callbackFactory);

        FilterawareDispatcher dispatcher = new DefaultFilterawareDispatcher(xsrfDispatcherFilter, cachingDispatcherFilter);

        return dispatcher;
    }

    public void purge() {
        this.cache.purge();
        this.xsrf.flush();
        GWT.log("XSRF:" + this.xsrf.getToken());
    }

}