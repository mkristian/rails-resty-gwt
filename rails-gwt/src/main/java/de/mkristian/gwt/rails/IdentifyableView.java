/**
 * 
 */
package de.mkristian.gwt.rails;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class IdentifyableView extends Composite {

    @UiField
    public Label id;

    protected int idCache;

    protected void resetSignature(int id){
        this.idCache = id;
        if(id > 0){
            this.id.setText("id: " + id);
        }
        else {
            this.id.setText(null);
        }
    }    
}