package de.mkristian.gwt.rails;

import com.google.inject.ImplementedBy;

@ImplementedBy( RemoteNotifierLabel.class )
public interface RemoteNotifier {

    void loading();

    void saving();

    void deleting();

    void finish();

}