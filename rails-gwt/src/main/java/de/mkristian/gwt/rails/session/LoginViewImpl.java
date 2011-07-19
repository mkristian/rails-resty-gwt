package de.mkristian.gwt.rails.session;



import com.google.gwt.event.dom.client.ClickEvent;
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
    public TextBox username;
    
    @UiField
    public PasswordTextBox password;
    
    private Presenter presenter;

    public LoginViewImpl(UiBinder<Widget, LoginViewImpl> uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("loginButton")
    public void onClickEdit(ClickEvent e) {
        presenter.login(username.getText(), password.getText());
        password.setText(null);
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
    
}
