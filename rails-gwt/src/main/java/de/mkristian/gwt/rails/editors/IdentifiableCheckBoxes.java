/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import de.mkristian.gwt.rails.models.HasToDisplay;
import de.mkristian.gwt.rails.models.Identifiable;

public class IdentifiableCheckBoxes<T extends Identifiable & HasToDisplay> extends ValueCheckBoxes<T> {

    public IdentifiableCheckBoxes() {
        super(new DisplayRenderer<T>(), new ProvidesId<T>());
    }
}