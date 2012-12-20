/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import com.google.gwt.user.client.ui.ListBox;

import de.mkristian.gwt.rails.models.HasToDisplay;
import de.mkristian.gwt.rails.models.Identifiable;

public class IdentifiableListBox<T extends Identifiable & HasToDisplay> extends ValueListBox<T> {

    public IdentifiableListBox() {
        super(new DisplayRenderer<T>(), new ProvidesId<T>());
    }
    
    public void setEnabled(boolean enabled){
        ((ListBox) getWidget()).setEnabled(enabled);
    }
    
    public void setName(String name){
        ((ListBox) getWidget()).setName(name); 
    }
}