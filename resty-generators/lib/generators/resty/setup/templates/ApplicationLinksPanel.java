package <%= base_package %>;

import javax.inject.Singleton;

import <%= models_package %>.Application;
import <%= models_package %>.User;
import com.google.inject.Inject;

import de.mkristian.gwt.rails.session.SessionManager;
import de.mkristian.gwt.rails.views.LinksPanel;

@Singleton
public class ApplicationLinksPanel extends LinksPanel<User> {

    @Inject
    ApplicationLinksPanel(SessionManager<User> sessionManager) {
        super(sessionManager);
    }

    @Override
    protected void initUser(User user) {
        for(Application app: user.applications){
            addLink(app.getName().equals("THIS") ? 
                    "users" : 
                    app.getName(), app.getUrl());
        }
    }

}