package de.mkristian.gwt.rails;

import com.google.gwt.place.shared.Place;

public abstract class RestfulPlace extends Place {

    public final RestfulAction action;

    public final int id;

    public final String resourceName;

    public RestfulPlace(RestfulAction restfulAction, String name) {
        this(0, restfulAction, name);
    }
    
    public RestfulPlace(int id, RestfulAction restfulAction, String name) {
        this.action = restfulAction;    
        this.id = id;
        this.resourceName = name;
    }
    
    public RestfulPlace(String id, RestfulAction restfulAction, String name) {
        this(id == null? 0 : Integer.parseInt(id), restfulAction, name);
    }
    
    public String identifier(){
        return "" + id;
    }   
}