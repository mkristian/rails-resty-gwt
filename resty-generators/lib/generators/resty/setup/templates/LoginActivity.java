package <%= activities_package %>;

import javax.inject.Inject;

import <%= models_package %>.User;
import <%= places_package %>.LoginPlace;
import <%= restservices_package %>.SessionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.assistedinject.Assisted;

import <%= gwt_rails_session_package %>.Authentication;
import <%= gwt_rails_session_package %>.LoginView;
import <%= gwt_rails_session_package %>.Session;
import <%= gwt_rails_session_package %>.SessionManager;

public class LoginActivity extends AbstractActivity implements LoginView.Presenter{

    private final SessionRestService service;
    private final LoginView view;
    private final SessionManager<User> sessionManager;

    @Inject
    public LoginActivity(@Assisted LoginPlace place,
            LoginView view,
            SessionRestService service,
            SessionManager<User> sessionManager) {
        this.view = view;
        this.service = service;
        this.sessionManager = sessionManager;
    }

    public void start(AcceptsOneWidget display, EventBus eventBus) {
        display.setWidget(view.asWidget());
        view.setPresenter(this);
    }

    public void login(final String login, String password) {
        Authentication authentication = new Authentication(login, password);
        service.create(authentication, new MethodCallback<Session<User>>() {

            public void onSuccess(Method method, Session<User> session) {
                GWT.log("logged in: " + login);
                sessionManager.login(session);
            }

            public void onFailure(Method method, Throwable exception) {
                GWT.log("login failed: " + exception.getMessage(), exception);
                sessionManager.accessDenied();
            }
        });
    }
}