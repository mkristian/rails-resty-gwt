/**
 * 
 */
package de.mkristian.gwt.rails;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class TimestampedView extends Composite {
    
    @UiField
    public Label createdAt;

    @UiField
    public Label updatedAt;
    
    protected Date createdAtCache;
    protected Date updatedAtCache;

    protected void resetSignature(Date createdAt, Date updatedAt) {
        this.createdAt.setText("created at: "
                + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM)
                        .format(createdAt));
        this.updatedAt.setText("updated at: "
                + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM)
                        .format(updatedAt));
        this.createdAtCache = createdAt;
        this.updatedAtCache = updatedAt;
    }
}