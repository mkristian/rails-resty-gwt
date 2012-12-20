package de.mkristian.gwt.rails.views;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

import de.mkristian.gwt.rails.editors.EnabledEditor;

public abstract class ReadOnlyViewImpl<T> extends Composite {

    @UiField public Button listButton;
    
    @UiField(provided = true) public Label header;
    @UiField(provided = true) public EnabledEditor<T> editor;

    protected final PlaceController places;
    protected final SimpleBeanEditorDriver<T, Editor<T>> editorDriver;

    public ReadOnlyViewImpl( String title,
            PlaceController places,
            final EnabledEditor<T> editor,
            SimpleBeanEditorDriver<T, Editor<T>> driver ) {
        this.header = new Label( title );
        this.places = places;     
        this.editor = editor;
        this.editorDriver = driver;
        this.editorDriver.initialize( editor );
    }

    protected abstract Place newListPlace();

    public void show( T model ) {
        editorDriver.edit( model );
        editor.setEnabled( false );
    }
    
    @UiHandler("listButton")
    public void onListClick(ClickEvent event) {
        places.goTo( newListPlace() );
    }
}