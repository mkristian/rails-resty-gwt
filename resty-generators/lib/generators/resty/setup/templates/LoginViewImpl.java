package <%= views_package %>;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class LoginViewImpl extends <%= gwt_rails_session_package %>.LoginViewImpl {

    @UiTemplate("LoginView.ui.xml")
    interface LoginViewUiBinder extends UiBinder<Widget,<%= gwt_rails_session_package %>.LoginViewImpl> {}

    private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

    public LoginViewImpl() {
        super(uiBinder);
    }

}