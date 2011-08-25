/**
 * 
 */
package de.mkristian.gwt.rails.session;

// TODO use eventbus paradigm
public interface SessionHandler<T> {
    
    void login(T user);
    
    void accessDenied();
    
    void timeout();

    void logout();
    
}