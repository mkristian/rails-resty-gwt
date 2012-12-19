package de.mkristian.gwt.rails.session;

public interface LoginPresenter {

    void login(String login, String password);

    void resetPassword(String login);
}