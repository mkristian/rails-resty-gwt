package de.mkristian.gwt.rails.models;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractQuery<T> implements Query<T> {

    protected static final String SEPARATOR = "|";

    protected AbstractQuery(String query){
        fromQuery(query);
    }   
    
    abstract protected void fromQuery(String query);
    
    abstract protected boolean match(T model);
    
    public List<T> filter(List<T> models) {
        List<T> result = new LinkedList<T>();
        for(T model: models){
            if (match(model)) {
                result.add(model);
            }
        }
        return result;
    }

    public int hashCode(){
        return toQuery().hashCode();
    }
    
    public boolean equals(Object other){
        return other instanceof Query<?> 
           && ((Query<?>) other).toQuery().equals(this);
    }
}
