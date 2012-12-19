package de.mkristian.gwt.rails.session;

import com.google.gwt.user.client.ui.IsWidget;

public interface LoginView extends IsWidget {
    void setPresenter(LoginPresenter presenter);
}