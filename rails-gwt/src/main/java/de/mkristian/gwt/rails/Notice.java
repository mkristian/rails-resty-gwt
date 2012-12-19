/**
 *
 */
package de.mkristian.gwt.rails;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Singleton;

@Singleton
public class Notice extends FlowPanel {
    
    private final InlineHTML notice = new InlineHTML();
    private final RemoteNotifier notifier;
    
    private final PopupPanel popup = new PopupPanel(true);
    
    @Inject
    public Notice(RemoteNotifierLabel notifier) {
        this.notifier = notifier;
        popup.setStyleName("gwt-rails-info");
        popup.setWidget(notice);
        popup.hide();
        popup.setVisible(false);
        add(notifier);
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

    @Deprecated
    public void loading(){
        GWT.log("DEPRECATED: use notifier directly");
        notifier.loading();
    }
    
    @Deprecated
    public void finishLoading(){
        GWT.log("DEPRECATED: use notifier directly");
        notifier.finish();
    }
 }