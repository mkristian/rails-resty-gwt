package de.mkristian.gwt.rails.session;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;

public abstract class AbstractLoginViewImpl extends Composite
        implements LoginView {

    private LoginPresenter presenter;
    
    @UiHandler({"login", "password"})
    public void onEnterLogin(KeyPressEvent e){
        if (e.getNativeEvent().getKeyCode() == 13) {
            login();
        }
    }
    
    @UiHandler("username")
    public void onEnterResetPassword(KeyPressEvent e){
        if (e.getNativeEvent().getKeyCode() == 13) { 
            resetPassword();
        }
    }

    public void setPresenter(LoginPresenter presenter) {
        this.presenter = presenter;
        getLogin().setValue(null);
    }

    void login(){
        presenter.login(getLogin().getValue(), clearPassword());
    }
    
    void resetPassword(){
        presenter.resetPassword(clearUsername());
    }
    
    String clearUsername() {
        String u = getUsername().getValue();
        getUsername().setValue(null);
        return u;
    }

    String clearPassword() {
        String p = getPassword().getValue();
        getPassword().setValue(null);
        return p;
    }
    
    abstract HasValue<String> getUsername();
    abstract HasValue<String> getPassword();
    abstract HasValue<String> getLogin();
}
