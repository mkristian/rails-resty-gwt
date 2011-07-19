package de.mkristian.gwt.rails.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;

import de.mkristian.gwt.rails.RestfulAction;


public class Session<T> {

    public int idle_session_timeout;
    
    public T user;
    
    public Set<Permission> permissions;
    
    transient private Map<String, Set<String>> map;
    
    boolean isAllowed(String resource, RestfulAction action){
        GWT.log("checking permission: " + resource + "#" + action.name().toLowerCase());
        Map<String, Set<String>> map = map();
        GWT.log(map.toString() + " -> " + this );
        if(map.containsKey(resource)){
            return map.get(resource).contains(action.name().toLowerCase());
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
