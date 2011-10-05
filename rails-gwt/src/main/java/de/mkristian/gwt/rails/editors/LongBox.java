/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import com.google.gwt.text.client.LongParser;
import com.google.gwt.text.client.LongRenderer;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBox;

public class LongBox extends ValueBox<Long> {

    public LongBox() {
        super(new TextBox().getElement(), LongRenderer.instance(), LongParser.instance());
    }
    
}