package de.mkristian.gwt.rails.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;

import de.mkristian.gwt.rails.RestfulAction;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

@Json(style = Style.RAILS)
public class Session<T> {

    @Json(name = "idle_session_timeout")
    public int idleSessionTimeout;
    
    public T user;
    
    public Set<Permission> permissions;
    
    transient private Map<String, Set<String>> map;
    
    boolean isAllowed(String resource, RestfulAction action){
        Map<String, Set<String>> map = map();
        if(map.containsKey(resource)){
            boolean result = map.get(resource).contains(action.name().toLowerCase());
            GWT.log("permission: " + resource + "#" + action.name().toLowerCase() + " -> " + result);
            return result;
        }
        else {
            GWT.log("unknown resource:" + resource);
        }
        return false;
    }
    
    private Map<String, Set<String>> map(){
        if(map == null){
            map = new HashMap<String, Set<String>>();
            for(Permission p: this.permissions){
                Set<String> actions = new TreeSet<String>();
                for(Action a: p.actions){
                    actions.add(a.name);
                }
                map.put(p.resource, actions);
            }
        }
        return map;
    }
}
