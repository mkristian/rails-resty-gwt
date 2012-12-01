package de.mkristian.gwt.rails;


public interface SessionApplication<T> extends Application{
    
    void startSession(T user);
    
    void stopSession();
    
}