/**
 * 
 */
package de.mkristian.gwt.rails;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class Application {
    
    protected final ActivityManager activityManager;
    
    protected Application(final ActivityManager activityManager){
      this.activityManager = activityManager;
    }
    
    public void run(){
        SimplePanel appWidget = new SimplePanel();
        activityManager.setDisplay(appWidget);
        getApplicationPanel().add(appWidget);
    }
    
    protected abstract Panel getApplicationPanel(); 
}