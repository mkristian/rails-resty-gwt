/**
 *
 */
package de.mkristian.gwt.rails.session;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;

import de.mkristian.gwt.rails.places.RestfulPlace;

@Singleton
public class SessionManager<T> {

    private Session<T> session;
    private final List<SessionHandler<T>> handlers = new ArrayList<SessionHandler<T>>();

    private Timer timer;

    public void addSessionHandler(SessionHandler<T> handler){
        this.handlers.add(handler);
    }

    public boolean hasSession(){
        return this.session != null;
    }

    public void login(Session<T> session){
        this.session = session;
        resetTimer();
        for(SessionHandler<T> handler: this.handlers){
            handler.login(session.user);
        }
        History.fireCurrentHistoryState();
    }

    public void logout(){
        this.session = null;
        resetTimer();
        for(SessionHandler<T> handler: this.handlers){
            handler.logout();
        }
        History.fireCurrentHistoryState();
        // TODO clear caches !!!
    }

    public boolean isAllowed(RestfulPlace<?> place) {
        return this.session.isAllowed(place.resourceName, place.action);
    }

    public void timeout(){
        session = null;
        for(SessionHandler<T> handler: this.handlers){
            handler.timeout();
        }
        History.fireCurrentHistoryState();
    }

    public void resetTimer() {
        if(this.timer != null){
            this.timer.cancel();
        }
        if(this.session != null && this.session.idleSessionTimeout > 0){
            this.timer = new Timer() {

                @Override
                public void run() {
                    timeout();
                }

            };
            this.timer.schedule(this.session.idleSessionTimeout * 60000);
        }
        else {
            this.timer = null;
        }
    }

    public void accessDenied() {
        for(SessionHandler<T> handler: this.handlers){
            handler.accessDenied();
        }
    }
}
