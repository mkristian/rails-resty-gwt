package de.mkristian.gwt.rails;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class AbstractApplication implements Application {

    protected final SimplePanel appPanel = new SimplePanel();

    protected AbstractApplication(final ActivityManager activityManager){
        activityManager.setDisplay(appPanel);
    }

    public void run(){
        initApplicationPanel(appPanel);
    }

    abstract protected void initApplicationPanel(Panel panel);
}
