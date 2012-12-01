package de.mkristian.gwt.rails.session;

import java.util.Set;

import de.mkristian.gwt.rails.places.RestfulAction;
import de.mkristian.gwt.rails.places.RestfulPlace;

public interface Guard extends HasSession {

    boolean isAllowed(RestfulPlace<?, ?> place);

    boolean isAllowed(String resourceName, RestfulAction action);

    boolean isAllowed(String resourceName, RestfulAction action,
            String association);

    boolean isAllowed(String resourceName, String action, String association);

    boolean isAllowed(String resourceName, String action);

    Set<String> allowedAssociations(String resourceName, RestfulAction action);

    Set<String> allowedAssociations(String resourceName, String action);

    Set<String> allowedAssociations(String resourceName);
    
    // TODO move to better place
    void resetCountDown();
}