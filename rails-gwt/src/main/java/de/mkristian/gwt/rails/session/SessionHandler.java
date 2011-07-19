/**
 * 
 */
package de.mkristian.gwt.rails.session;

public interface SessionHandler<T> {
    
    void login(T user);
    
    void accessDenied();
    
    void timeout();

    void logout();
    
}