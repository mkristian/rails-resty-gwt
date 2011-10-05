/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import com.google.gwt.text.shared.AbstractRenderer;

import de.mkristian.gwt.rails.models.HasToDisplay;

public class DisplayRenderer<T extends HasToDisplay> extends AbstractRenderer<T> {

    public String render(T object) {
        return object.toDisplay();
    }
    
}