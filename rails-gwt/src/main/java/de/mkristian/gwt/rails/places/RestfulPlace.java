package de.mkristian.gwt.rails.places;

import com.google.gwt.place.shared.Place;

public abstract class RestfulPlace<T> extends Place {

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.name().hashCode());
        result = prime * result + id;
        result = prime * result + (hasSession ? 1231 : 1237);
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result
                + ((resourceName == null) ? 0 : resourceName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RestfulPlace<?> other = (RestfulPlace<?>) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.name().equals(other.action.name()))
            return false;
        if (id != other.id)
            return false;
        if (hasSession != other.hasSession)
            return false;
        if (model == null) {
            if (other.model != null)
                return false;
        } else if (!model.equals(other.model))
            return false;
        if (resourceName == null) {
            if (other.resourceName != null)
                return false;
        } else if (!resourceName.equals(other.resourceName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Place[action=" + action + ", id=" + id + ", model="
                + model + ", resourceName=" + resourceName + "]";
    }

    public final RestfulAction action;

    public final int id;

    public final String resourceName;

    public final T model;
    
    public boolean hasSession = true;
    
    public RestfulPlace(RestfulAction restfulAction, String name) {
        this(0, restfulAction, name);
    }
    
    public RestfulPlace(T model, RestfulAction restfulAction, String name) {
        this.action = restfulAction;    
        this.id = 0;
        this.model = model;
        this.resourceName = name;
    }

    public RestfulPlace(int id, RestfulAction restfulAction, String name) {
        this.action = restfulAction;    
        this.id = id;
        this.resourceName = name;
        this.model = null;
    }
    
    public RestfulPlace(String id, RestfulAction restfulAction, String name) {
        this(id == null? 0 : Integer.parseInt(id), restfulAction, name);
    }    
}