package de.mkristian.gwt.rails.session;

import com.google.inject.ImplementedBy;

@ImplementedBy(value=SessionManager.class)
public interface HasSession {
    boolean hasSession();
}