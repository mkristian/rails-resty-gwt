package de.mkristian.gwt.rails;

import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.example.client.dispatcher.DispatcherFactory;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;

public class RestfulRetryingDispatcherSingleton implements Dispatcher {
    
    // we just want this singleton instance nothing else !!!
    public static Dispatcher INSTANCE = new DispatcherFactory().restfulCachingDispatcher();

    // do not allow concrete instances of this class
    private RestfulRetryingDispatcherSingleton(){
        throw new Error("never called");
    }
    
    // dummy
    public Request send(Method method, RequestBuilder builder)
            throws RequestException {
        return null;
    }
    
}
