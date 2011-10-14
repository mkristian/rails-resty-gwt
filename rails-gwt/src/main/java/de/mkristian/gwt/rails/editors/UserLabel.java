/**
 * 
 */
package de.mkristian.gwt.rails.editors;

import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.ValueLabel;

import de.mkristian.gwt.rails.models.IsUser;

public class UserLabel<T extends IsUser> extends ValueLabel<T> {
 
    static class UserRenderer extends AbstractRenderer<IsUser>{

        public String render(IsUser user) {
            return user == null ? "" : user.getName() + " (" + user.getLogin() + ")";
        }
    }

    private final static UserRenderer RENDERER = new UserRenderer();
    
    public UserLabel() {
        super(RENDERER);
    }        
}