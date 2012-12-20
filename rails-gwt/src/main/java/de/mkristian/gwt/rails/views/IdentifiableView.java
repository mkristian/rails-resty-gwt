/**
 *
 */
package de.mkristian.gwt.rails.views;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.NumberLabel;

public class IdentifiableView extends Composite {

    @UiField
    public NumberLabel<Integer> id;

    @UiField
    public FlowPanel signature;

    protected void resetSignature(int id){
        if(id > 0){
            this.id.setValue(id);
            this.signature.setVisible(true);
        }
        else {
            this.id.setValue(null);
            this.signature.setVisible(false);
        }
    }
}