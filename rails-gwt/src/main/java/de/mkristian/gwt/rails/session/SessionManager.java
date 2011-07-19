/**
 * 
 */
package de.mkristian.gwt.rails.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Singleton;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

import de.mkristian.gwt.rails.RestfulAction;
import de.mkristian.gwt.rails.RestfulPlace;

@Singleton
public class SessionManager<T> {
    
    private Session<T> session;
    private SessionHandler<T> handler;

    //TODO move this part into the session once @JsonIgnored is recognised
    private Map<String, Set<String>> map;
    private Timer timer;
        
    public void addSessionHandler(SessionHandler<T> handler){
        if (this.handler != null)
            throw new RuntimeException("not implemented to have more than one handler");
        this.handler = handler;
    }
 
    public boolean isActive(){
        return this.session != null;
    }
    
    public void login(Session<T> session){
        this.session = session;
        resetTimer();
        if(this.handler != null){
            this.handler.login(session.user);
        }
    }
    
    public void logout(){
        this.session = null;
        this.map = null;
        resetTimer();
        if(this.handler != null){
            this.handler.logout();
        }
        // TODO clear caches !!!
    }

    public boolean isAllowed(RestfulPlace place) {
        return isAllowed(place.resourceName, place.action);
    }

    public boolean isAllowed(RestfulPlace place, RestfulAction action) {
        return isAllowed(place.resourceName, action);
    }
   
    private boolean isAllowed(String resource, RestfulAction action){
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
            for(Permission p: this.session.permissions){
                Set<String> actions = new TreeSet<String>();
                for(Action a: p.actions){
                    actions.add(a.name);
                }
                map.put(p.resource, actions);
            }
        }
        return map;
    }
 
    public void timeout(){
        session = null;
        map = null;
        if (handler != null) {
            handler.timeout();
        }
    }

    public void resetTimer() {
        if(this.timer != null){
            this.timer.cancel();
        }
        if(this.session != null){
            this.timer = new Timer() {

                @Override
                public void run() {
                    timeout();
                }

            };
            this.timer.schedule(session.idle_session_timeout * 60000);
        }
        else {
            this.timer = null;
        }
    }

    public void accessDenied() {
        if( this.handler != null){
            this.handler.accessDenied();
        }
    }
}