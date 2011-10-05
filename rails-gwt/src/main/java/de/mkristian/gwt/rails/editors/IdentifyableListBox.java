/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueListBox;

import de.mkristian.gwt.rails.models.HasToDisplay;
import de.mkristian.gwt.rails.models.Identifyable;

public class IdentifyableListBox<T extends Identifyable & HasToDisplay> extends ValueListBox<T> {

    public IdentifyableListBox() {
        super(new DisplayRenderer<T>(), new ProvidesId<T>());
    }
    
    public void setEnabled(boolean enabled){
        ((ListBox) getWidget()).setEnabled(enabled);
    }
}