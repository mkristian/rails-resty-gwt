/**
 * 
 */
package de.mkristian.gwt.rails.session;

import javax.inject.Singleton;

import com.google.gwt.user.client.Timer;

import de.mkristian.gwt.rails.RestfulAction;
import de.mkristian.gwt.rails.RestfulPlace;

@Singleton
public class SessionManager<T> {
    
    private Session<T> session;
    private SessionHandler<T> handler;

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
        resetTimer();
        if(this.handler != null){
            this.handler.logout();
        }
        // TODO clear caches !!!
    }

    public boolean isAllowed(RestfulPlace place) {
        return this.session.isAllowed(place.resourceName, place.action);
    }

    public boolean isAllowed(RestfulPlace place, RestfulAction action) {
        return this.session.isAllowed(place.resourceName, action);
    }
   
 
    public void timeout(){
        session = null;
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