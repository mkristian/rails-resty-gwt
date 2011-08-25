/**
 * 
 */
package de.mkristian.gwt.rails.views;

import java.util.Date;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.NumberLabel;

public class IdentifyableTimestampedView extends TimestampedView {

    @UiField
    public NumberLabel<Integer> id;

    protected void resetSignature(int id, Date createdAt, Date updatedAt){
        if(id > 0){
            resetSignature(createdAt, updatedAt);
            this.id.setValue(id);
        }
        else {
            this.createdAt.setValue(null);
            this.updatedAt.setValue(null);
            this.id.setValue(null);
            this.signature.setVisible(false);
        }
    }    
}