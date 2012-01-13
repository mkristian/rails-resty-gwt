package de.mkristian.gwt.rails.views;


import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import de.mkristian.gwt.rails.session.SessionHandler;
import de.mkristian.gwt.rails.session.SessionManager;

public abstract class LinksPanel<T> extends FlowPanel {
    
    public static interface LinkTemplate extends SafeHtmlTemplates {
        @Template("<a href=\"{1}\">{0}</a>")
        SafeHtml link(String name, SafeUri url);
    }
    private static final LinksPanel.LinkTemplate TEMPLATE = GWT.create(LinksPanel.LinkTemplate.class);
    
    protected LinksPanel(final SessionManager<T> sessionManager){
        setStyleName("gwt-rails-application-links");
        sessionManager.addSessionHandler(new SessionHandler<T>() {
            
            public void timeout() {
                clear();
                setVisible(false);
            }
            
            public void logout() {
                clear();
                setVisible(false);
            }
            
            public void login(T user) { 
                initUser(user);
                setVisible(true);
            }
            
            public void accessDenied() {                
            }
        });
    }
    
    abstract protected void initUser(T user);
        
    public void addLink(String name, String url){
        add(new HTML(TEMPLATE.link(name, UriUtils.fromString(url))));
    }
}