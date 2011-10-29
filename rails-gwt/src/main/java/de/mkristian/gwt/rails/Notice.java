/**
 *
 */
package de.mkristian.gwt.rails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Singleton;

@Singleton
public class Notice extends FlowPanel {
    
    private final Label notice = new Label();
    private final Label loading = new Label("loading");
    
    private final PopupPanel popup = new PopupPanel(true);
    
    public Notice() {
        loading.setStyleName("gwt-rails-loading");
        loading.setVisible(false);
        popup.setStyleName("gwt-rails-info");
        popup.setWidget(notice);
        popup.hide();
        popup.setVisible(false);
        add(loading);
    }

    @Deprecated
    public void setText(String msg){
        GWT.log("DEPRECATED: use error, info and warn instead");
        show(msg);
    }

    private void show(String msg) {
        popup.setVisible(true);
        notice.setText(msg);
        if (msg == null) {
            popup.hide();
        } else {
            popup.show();
        }
    }
    
    public void info(String msg){
        show(msg);
        popup.setStyleName("gwt-rails-info");
    }

    public void warn(String msg){
        show(msg);
        popup.setStyleName("gwt-rails-warn");
    }
    
    public void error(String msg){
        show(msg);
        popup.setStyleName("gwt-rails-error");
    }

    public void error(String msg, Throwable e){
        error(msg + " - " + e.getMessage());
    }

    public void hide(){
        popup.hide();
    }
    
    public void loading(){
        loading.setVisible(true);
    }

    public void finishLoading(){
        loading.setVisible(false);
    }
}