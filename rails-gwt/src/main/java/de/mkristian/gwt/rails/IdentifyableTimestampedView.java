/**
 * 
 */
package de.mkristian.gwt.rails;

import java.util.Date;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;

public class IdentifyableTimestampedView extends TimestampedView {

    @UiField
    public Label id;

    protected int idCache;

    protected void resetSignature(int id, Date createdAt, Date updatedAt){
        this.idCache = id;
        if(id > 0){
            resetSignature(createdAt, updatedAt);
            this.id.setText("id: " + id);
        }
        else {
            this.createdAt.setText(null);
            this.updatedAt.setText(null);
            this.id.setText(null);
        }
    }    
}