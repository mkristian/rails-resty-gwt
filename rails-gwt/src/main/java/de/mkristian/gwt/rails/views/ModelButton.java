/**
 * 
 */
package de.mkristian.gwt.rails.views;

import com.google.gwt.user.client.ui.Button;

import de.mkristian.gwt.rails.places.RestfulActionEnum;

public class ModelButton<T> extends Button {
    public final T model;
    public final RestfulActionEnum action;
    
    public ModelButton(RestfulActionEnum action, T model){
        super(action.name().substring(0, 1) + action.name().substring(1).toLowerCase());
        this.model = model;
        this.action = action;
    }
}