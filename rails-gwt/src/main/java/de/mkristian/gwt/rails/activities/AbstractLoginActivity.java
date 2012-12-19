package de.mkristian.gwt.rails.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import de.mkristian.gwt.rails.session.LoginPresenter;
import de.mkristian.gwt.rails.session.LoginView;

public abstract class AbstractLoginActivity extends AbstractActivity {

    protected final LoginView view;

    public AbstractLoginActivity( LoginView view,
                LoginPresenter presenter ) {
        this.view = view;
        this.view.setPresenter( presenter );
    }

    public void start(AcceptsOneWidget display, EventBus eventBus) {
        display.setWidget( view.asWidget() );
    }

}