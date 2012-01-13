/**
 *
 */
package de.mkristian.gwt.rails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Singleton;

@Singleton
public class Notice extends FlowPanel {
    
    private final InlineHTML notice = new InlineHTML();
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

    private void show(final String msg) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            
            public void execute() {                
                popup.setVisible(true);
                if (msg == null) {
                    popup.hide();
                } 
                else {
                    SafeHtmlBuilder buf = new SafeHtmlBuilder();
                    buf.appendEscapedLines(msg.trim());
                    notice.setHTML(buf.toSafeHtml());
                    popup.show();
                }
            }
        });
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