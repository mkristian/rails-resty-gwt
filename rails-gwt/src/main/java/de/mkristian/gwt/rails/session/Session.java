package de.mkristian.gwt.rails.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;

import de.mkristian.gwt.rails.places.RestfulAction;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

@Json(style = Style.RAILS)
public class Session<T> {

    @Json(name = "idle_session_timeout")
    public int idleSessionTimeout;

    public T user;

    public Set<Permission> permissions;

    transient private Map<String, Set<String>> allow;
    transient private Map<String, Set<String>> deny;

    boolean isAllowed(String resource, RestfulAction action) {
        return isAllowed(resource, action.name().toLowerCase());
    }
    
    boolean isAllowed(String resource, String actionName) {
        final boolean result;
        Map<String, Set<String>> map = allow();
        if (map.containsKey(resource)) {
            result = map.get(resource).contains(actionName);
        }
        else {
            map = deny();
            if (map.containsKey(resource)) {
                result = !map.get(resource).contains(actionName);
            }
            else {
                GWT.log("unknown resource:" + resource);
                return false;
            }
        }
        GWT.log("permission: " + resource + "#"
                + actionName + " -> " + result);
        return result;
    }

    private Map<String, Set<String>> allow(){
        if(allow == null || deny == null){
            createAllowAndDeny();
        }
        return allow;
    }

    private Map<String, Set<String>> deny(){
        if(allow == null || deny == null){
            createAllowAndDeny();
        }
        return deny;
    }

    private void createAllowAndDeny() {
        allow = new HashMap<String, Set<String>>();
        deny = new HashMap<String, Set<String>>();
        for(Permission p: this.permissions){
            Set<String> actions = new TreeSet<String>();
            for(Action a: p.actions){
                actions.add(a.name);
            }
            if(p.deny){
                deny.put(p.resource, actions);
            }
            else{
                allow.put(p.resource, actions);
            }
        }
    }
}
