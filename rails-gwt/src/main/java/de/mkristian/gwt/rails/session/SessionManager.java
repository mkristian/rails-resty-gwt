/**
 *
 */
package de.mkristian.gwt.rails.session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.History;

import de.mkristian.gwt.rails.caches.Cache;
import de.mkristian.gwt.rails.dispatchers.DispatcherFactory;
import de.mkristian.gwt.rails.places.RestfulAction;
import de.mkristian.gwt.rails.places.RestfulPlace;

@Singleton
public class SessionManager<T> {

    private Session<T> session;

    private final List<SessionHandler<T>> handlers = new ArrayList<SessionHandler<T>>();

    private int countdown;

    private Set<Cache> caches = new HashSet<Cache>();

    public void addSessionHandler(SessionHandler<T> handler){
        this.handlers.add(handler);
    }

    public boolean hasSession(){
        return this.session != null;
    }

    public Session<T> getSession(){
        return this.session;
    }

    public void login(Session<T> session){
        this.session = session;
        runTimer();
        for(SessionHandler<T> handler: this.handlers){
            handler.login(session.user);
        }
        History.fireCurrentHistoryState();
    }

    public void logout(){
        this.session = null;
        countdown = -1;
        purgeCaches();
        for(SessionHandler<T> handler: this.handlers){
            handler.logout();
        }
        History.fireCurrentHistoryState();
    }

    private void purgeCaches() {
        for(Cache c: caches){
            c.purge();
        }
        DispatcherFactory.INSTANCE.purge();
    }

    public boolean isAllowed(RestfulPlace<?, ?> place) {
        return this.session.isAllowed(place.resourceName, place.action.name().toLowerCase(), null);
    }

    public boolean isAllowed(String resourceName, RestfulAction action) {
        return this.session.isAllowed(resourceName, action.name().toLowerCase(), null);
    }

    public boolean isAllowed(String resourceName, RestfulAction action, String association) {
        return this.session.isAllowed(resourceName, action.name().toLowerCase(), association);
    }

    public boolean isAllowed(String resourceName, String action, String association) {
        return this.session.isAllowed(resourceName, action, association);
    }

    public boolean isAllowed(String resourceName, String action) {
        return this.session.isAllowed(resourceName, action, null);
    }

    public Set<String> allowedAssociations(String resourceName, RestfulAction action) {
        return this.session.allowedAssocations(resourceName, action.name().toLowerCase());
    }

    public Set<String> allowedAssociations(String resourceName, String action) {
        return this.session.allowedAssocations(resourceName, action);
    }

    public Set<String> allowedAssociations(String resourceName) {
        return this.session.allowedAssocations(resourceName);
    }

    public void timeout(){
        purgeCaches();
        session = null;
        for(SessionHandler<T> handler: this.handlers){
            handler.timeout();
        }
        History.fireCurrentHistoryState();      
    }

    private void runTimer() {
        countdown = this.session.idleSessionTimeout;
        Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
            
            public boolean execute() {
                GWT.log("idle timeout: " + countdown + " minutes left");
                if(countdown == 0){
                    timeout();
                }
                countdown--;
                return countdown > -1;
            }
        }, 60000);
    }

    @Deprecated
    public void resetTimer(){
        GWT.log("DEPRECATED use resetCountDown() instead");
        resetCountDown();
    }
    
    public void resetCountDown(){
        countdown = this.session.idleSessionTimeout;
    }

    public void accessDenied() {
        purgeCaches();
        for(SessionHandler<T> handler: this.handlers){
            handler.accessDenied();
        }
    }
    
    public void addCache(Cache cache){
        this.caches.add(cache);
    }
}
