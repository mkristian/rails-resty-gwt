/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import de.mkristian.gwt.rails.models.HasToDisplay;
import de.mkristian.gwt.rails.models.Identifyable;

public class IdentifyableCheckBoxes<T extends Identifyable & HasToDisplay> extends ValueCheckBoxes<T> {

    public IdentifyableCheckBoxes() {
        super(new DisplayRenderer<T>(), new ProvidesId<T>());
    }
}