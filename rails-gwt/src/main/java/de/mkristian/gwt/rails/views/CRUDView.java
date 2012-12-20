package de.mkristian.gwt.rails.views;

import com.google.gwt.user.client.ui.IsWidget;

import de.mkristian.gwt.rails.models.Identifiable;
import de.mkristian.gwt.rails.presenters.CRUDPresenter;

public interface CRUDView<T extends Identifiable> extends IsWidget {

    void setPresenter(CRUDPresenter<T> presenter);

    void show(T model);

    void edit(T model);

    void reset(T model);

    void showNew();

    boolean isDirty();
}