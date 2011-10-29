package de.mkristian.gwt.rails.session;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl extends Composite
        implements LoginView {

    @UiField
    public Button loginButton;

    @UiField
    public Button resetPasswordButton;

    @UiField
    public TextBox username;

    @UiField
    public TextBox login;

    @UiField
    public PasswordTextBox password;

    private Presenter presenter;

    public LoginViewImpl(UiBinder<Widget, LoginViewImpl> uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiHandler({"login", "password"})
    public void onEnterLogin(KeyPressEvent e){
        if (e.getNativeEvent().getKeyCode() == 13) {
            onClickLogin(null);
        }
    }
    
    @UiHandler("username")
    public void onEnterResetPassword(KeyPressEvent e){
        GWT.log(e.getCharCode()+" <-> " + ((int)e.getCharCode()) + " " + e.getNativeEvent().getKeyCode());
        if (e.getNativeEvent().getKeyCode() == 13) { 
            onClickResetPassword(null);
        }
    }
    
    @UiHandler("loginButton")
    public void onClickLogin(ClickEvent e) {
        presenter.login(login.getText(), password.getText());
        password.setText(null);
    }

    @UiHandler("resetPasswordButton")
    public void onClickResetPassword(ClickEvent e) {
        presenter.resetPassword(username.getText());
        username.setText(null);
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        this.login.setText(null);
    }
}
