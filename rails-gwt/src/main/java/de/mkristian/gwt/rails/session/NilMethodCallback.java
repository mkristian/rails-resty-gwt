package de.mkristian.gwt.rails.session;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class NilMethodCallback implements MethodCallback<Void> {
    
    public void onSuccess(Method method, Void response) {
    }
    
    public void onFailure(Method method, Throwable exception) {
    }
}