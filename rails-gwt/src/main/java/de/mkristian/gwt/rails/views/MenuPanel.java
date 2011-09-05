/**
 *
 */
package de.mkristian.gwt.rails.views;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import de.mkristian.gwt.rails.session.SessionHandler;
import de.mkristian.gwt.rails.session.SessionManager;

public class MenuPanel<T> extends FlowPanel {

    protected MenuPanel(){
        setStyleName("menu");
        setVisible(false);
    }

    protected MenuPanel(SessionManager<T> sessionManager){
        this();
        sessionManager.addSessionHandler(new SessionHandler<T>() {

            public void timeout() {
                setVisible(false);
            }

            public void logout() {
                setVisible(false);
            }

            public void login(T user) {
                setVisible(true);
            }

            public void accessDenied() {
            }
        });
    }

    protected Button addButton(String name) {
        Button button = new Button(name);
        add(button);
        return button;
    }
}