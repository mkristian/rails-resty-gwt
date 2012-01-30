/**
 *
 */
package de.mkristian.gwt.rails.views;

import com.google.gwt.user.client.ui.Button;

import de.mkristian.gwt.rails.places.RestfulAction;

public class GeneralModelButton<T, S extends RestfulAction> extends Button {
    public final T model;
    public final S action;
    
    public GeneralModelButton(S action, T model){
        super(action.name().substring(0, 1).toUpperCase() + action.name().substring(1).toLowerCase());
        this.model = model;
        this.action = action;
    }
}