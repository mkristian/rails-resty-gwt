package de.mkristian.ixtlan.gwt.errors;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mkristian.gwt.rails.editors.EnabledEditor;


public class ErrorEditor extends EnabledEditor<Error>{
    
    interface Binder extends UiBinder<Widget, ErrorEditor> {}

    private static final Binder BINDER = GWT.create(Binder.class);
    
    @Ignore @UiField FlowPanel signature;

    @UiField NumberLabel<Integer> id;
    @UiField DateLabel createdAt;

    @UiField TextBox message;

    @UiField Label request;

    @UiField Label response;

    @UiField Label session;

    @UiField Label parameters;

    @UiField TextBox clazz;

    @UiField Label backtrace;

    public ErrorEditor() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public void resetVisibility() {
        this.signature.setVisible(id.getValue() != null && id.getValue() > 0);
    }

    public void setEnabled(boolean enabled) {
        resetVisibility();
        this.message.setEnabled(enabled);
        this.clazz.setEnabled(enabled);
    }
}