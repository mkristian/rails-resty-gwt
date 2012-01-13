package de.mkristian.gwt.rails;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.FailedStatusCodeException;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


@Singleton
public class DisplayErrors {

    public enum Type {
        PRECONDITIONS, CONFLICT, GENERAL
    }
    
    private final Notice notice;

    @Inject
    public DisplayErrors(Notice notice){
        this.notice = notice;
    }
    
    public Type showMessages(Method method, Throwable exp) {
        if( exp instanceof FailedStatusCodeException){
            switch(((FailedStatusCodeException)exp).getStatusCode()){
                case 422:
                    JSONObject obj = JSONParser.parseStrict(method.getResponse().getText()).isObject();
                    if (obj != null){
                        StringBuffer buf = new StringBuffer();
                        for(String key : obj.keySet()) {
                            buf.append(key)
                                .append(": ")
                                .append(obj.get(key).toString().replaceAll("\\[\\]", ""))
                                .append("\n");
                        }
                        notice.error(buf.toString());
                    }
                    else {
                        // TODO
                    }
                    return Type.PRECONDITIONS;
                case 409:
                    // TODO
                    return Type.CONFLICT;
                default:
                    // TODO
            }
        }
        return Type.GENERAL;
    }
}