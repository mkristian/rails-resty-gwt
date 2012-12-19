package de.mkristian.gwt.rails.presenters;

import org.fusesource.restygwt.client.Method;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import de.mkristian.gwt.rails.events.ModelEvent;
import de.mkristian.gwt.rails.places.RestfulAction;

public interface Presenter {

    void setDisplay(AcceptsOneWidget display);

    void unknownAction(RestfulAction action);

    void unknownAction(ModelEvent.Action action);

    void onError(Method method, Throwable e);

}