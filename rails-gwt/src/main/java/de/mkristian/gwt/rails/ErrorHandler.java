package de.mkristian.gwt.rails;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.FailedStatusCodeException;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


@Singleton
public class ErrorHandler {

    public enum Type {
        FORBIDDEN, NOT_FOUND, CONFLICT, UNPROCESSABLE_ENTITY, GENERAL, UNDEFINED
    }
    
    private final Notice notice;

    @Inject
    public ErrorHandler(Notice notice){
        this.notice = notice;
    }
    
    public Type getType(Throwable exp){
        if( exp instanceof FailedStatusCodeException){
            switch(((FailedStatusCodeException)exp).getStatusCode()){
                case 422: return Type.UNPROCESSABLE_ENTITY;
                case 409: return Type.CONFLICT;
                case 403: return Type.FORBIDDEN;
                case 404: return Type.NOT_FOUND;
                default: return Type.UNDEFINED;
            }
        }
        return Type.GENERAL;
    }

    public void onError(Method method, Type type) {
        switch(type){
            case GENERAL:
                generalError(method);
            case UNDEFINED:
                undefinedStatus(method);
                break;
            case FORBIDDEN:
                forbidden(method);
                break;
            case NOT_FOUND:
                notFound(method);
                break;
            case CONFLICT:
                conflict(method);
                break;
            case UNPROCESSABLE_ENTITY:
                unprocessableEntity(method);
                break;
        }
    }

    protected void generalError(Method method) {
        show("Error");
    }

    protected void undefinedStatus(Method method) {
        if (method != null) {
            show("Error: " + method.getResponse().getStatusText());
        }
        else {
            show("Error");
        }
    }

    protected void conflict(Method method) {
        show("Conflict! Data was modified by someone else. Please reload the data.");
    }

    protected void unprocessableEntity(Method method) {
        showErrors(method);
    }

    protected void forbidden(Method method) {
        show("Forbidden.");
    }

    protected void notFound(Method method) {
        show("Resource not found.");
    }
    
    protected void showErrors(Method method){
        String text = method.getResponse().getText();
        JSONObject obj = JSONParser.parseStrict(text).isObject();
        if (obj != null){
            if (obj.containsKey("errors")){
                obj = obj.get("errors").isObject();
            }
            StringBuffer buf = new StringBuffer();
            for(String key : obj.keySet()) {
                buf.append(key)
                    .append(": ")
                    .append(obj.get(key).toString().replaceAll("\\[|\"|\\]", ""))
                    .append("\n");
            }
            notice.error(buf.toString());
        }
        else {
            notice.error(text);
        }
    }
    
    public void show(String text){
        notice.error(text);
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
                    return Type.UNPROCESSABLE_ENTITY;
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