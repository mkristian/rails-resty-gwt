/**
 * 
 */
package de.mkristian.gwt.rails.views;

import java.util.Date;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;

public class TimestampedView extends Composite {
    
    @UiField
    public DateLabel createdAt;

    @UiField
    public DateLabel updatedAt;
    
    @UiField
    public FlowPanel signature;

    protected void resetSignature(Date createdAt, Date updatedAt) {
        this.signature.setVisible(createdAt != null);
        this.createdAt.setValue(createdAt);
        this.updatedAt.setValue(updatedAt);
    }
}