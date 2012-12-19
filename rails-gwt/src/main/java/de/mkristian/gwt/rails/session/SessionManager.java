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
public class SessionManager<T> implements HasSession, Guard, CacheManager {

    private Session<T> session;

    private final List<SessionHandler<T>> handlers = new ArrayList<SessionHandler<T>>();

    private int countdown;

    private Set<Cache<?>> caches = new HashSet<Cache<?>>();

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
        for(SessionHandler<T> handler: this.handlers){
            handler.logout();
        }
        countdown = -1;
        purgeCaches();
        History.fireCurrentHistoryState();
    }

    public void purgeCaches() {
        for(Cache<?> c: caches){
            c.purgeAll();
        }
        DispatcherFactory.INSTANCE.purge();
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#isAllowed(de.mkristian.gwt.rails.places.RestfulPlace)
     */
    public boolean isAllowed(RestfulPlace<?, ?> place) {
        return this.session.isAllowed(place.resourceName, place.action.name().toLowerCase(), null);
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#isAllowed(java.lang.String, de.mkristian.gwt.rails.places.RestfulAction)
     */
    public boolean isAllowed(String resourceName, RestfulAction action) {
        return this.session.isAllowed(resourceName, action.name().toLowerCase(), null);
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#isAllowed(java.lang.String, de.mkristian.gwt.rails.places.RestfulAction, java.lang.String)
     */
    public boolean isAllowed(String resourceName, RestfulAction action, String association) {
        return this.session.isAllowed(resourceName, action.name().toLowerCase(), association);
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#isAllowed(java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean isAllowed(String resourceName, String action, String association) {
        return this.session.isAllowed(resourceName, action, association);
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#isAllowed(java.lang.String, java.lang.String)
     */
    public boolean isAllowed(String resourceName, String action) {
        return this.session.isAllowed(resourceName, action, null);
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#allowedAssociations(java.lang.String, de.mkristian.gwt.rails.places.RestfulAction)
     */
    public Set<String> allowedAssociations(String resourceName, RestfulAction action) {
        return this.session.allowedAssocations(resourceName, action.name().toLowerCase());
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#allowedAssociations(java.lang.String, java.lang.String)
     */
    public Set<String> allowedAssociations(String resourceName, String action) {
        return this.session.allowedAssocations(resourceName, action);
    }

    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.Guard#allowedAssociations(java.lang.String)
     */
    public Set<String> allowedAssociations(String resourceName) {
        return this.session.allowedAssocations(resourceName);
    }

    public void timeout(){
        session = null;
        for(SessionHandler<T> handler: this.handlers){
            handler.timeout();
        }
        countdown = -1;
        purgeCaches();
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
        for(SessionHandler<T> handler: this.handlers){
            handler.accessDenied();
        }
    }
    
    /* (non-Javadoc)
     * @see de.mkristian.gwt.rails.session.CacheManager#addCache(de.mkristian.gwt.rails.caches.Cache)
     */
    public void addCache(Cache<?> cache){
        this.caches.add(cache);
    }
}
