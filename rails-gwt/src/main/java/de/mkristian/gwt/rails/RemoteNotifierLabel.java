/**
 *
 */
package de.mkristian.gwt.rails;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Singleton;

@Singleton
public class RemoteNotifierLabel extends Label implements RemoteNotifier {
    
    public RemoteNotifierLabel() {
        setStyleName("gwt-rails-loading");
        setVisible(false);
    }
    
    public void loading(){
        setText("loading");
        setVisible(true);
    }

    public void saving(){
        setText("saving");
        setVisible(true);
    }
    
    public void deleting(){
        setText("deleting");
        setVisible(true);
    }
    
    public void finish(){
        setVisible(false);
    }
}