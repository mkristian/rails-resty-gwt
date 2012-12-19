package de.mkristian.gwt.rails.views;


import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public abstract class LinksPanel<T extends ExternalApplication> extends FlowPanel {
    
    public static interface LinkTemplate extends SafeHtmlTemplates {
        @Template("<a href=\"{1}\">{0}</a>")
        SafeHtml link(String name, SafeUri url);
    }
    private static final LinksPanel.LinkTemplate TEMPLATE = GWT.create(LinksPanel.LinkTemplate.class);
    
    protected LinksPanel(){
        setStyleName("gwt-rails-application-links");
    }
    
    public void initApplications(List<T> applications){
        if (applications != null) {
            for(ExternalApplication app: applications){
                addLink(app.getName(), app.getUrl());
            }
            setVisible(true);
        }
        else {
            clear();
            setVisible(false);
        }
    }
        
    public void addLink(String name, String url){
        add(new HTML(TEMPLATE.link(name, UriUtils.fromString(url))));
    }
}