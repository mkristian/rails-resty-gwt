/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import com.google.gwt.view.client.ProvidesKey;

import de.mkristian.gwt.rails.models.Identifiable;

public class ProvidesId<T extends Identifiable> implements ProvidesKey<T>{

    public Object getKey(T item) {
        return item == null? 0 : item.getId();
    }
    
}