package de.mkristian.gwt.rails.views;

import static de.mkristian.gwt.rails.places.RestfulActionEnum.EDIT;

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
import de.mkristian.gwt.rails.places.RestfulActionEnum;
import de.mkristian.gwt.rails.presenters.SingletonPresenter;
import de.mkristian.gwt.rails.session.Guard;

public abstract class SingletonViewImpl<T> extends Composite {

    @UiField public Button reloadButton;
    @UiField public Button editButton;
    @UiField public Button cancelButton;
    @UiField public Button saveButton;

    @UiField(provided = true) public Label header;
    @UiField(provided = true) public EnabledEditor<T> editor;
    
    private SingletonPresenter<T> presenter;
    
    protected final Guard guard;
    protected final PlaceController places;
    protected final SimpleBeanEditorDriver<T, Editor<T>> editorDriver;
//    private T model;

    public SingletonViewImpl( final String title, 
            final Guard guard,
            final PlaceController places, 
            final EnabledEditor<T> editor,
            final SimpleBeanEditorDriver<T, Editor<T>> driver) {
        this.header = new Label( title );
        this.guard = guard;
        this.places = places; 
        this.editor = editor;      
        this.editorDriver = driver;
        this.editorDriver.initialize( editor );
    }

    protected abstract String placeName();
    protected abstract Place showPlace(T model);
    protected abstract Place editPlace(T model);

    protected boolean isAllowed( RestfulActionEnum action ){
        return guard.isAllowed( placeName(), action );
    }
    
    public void setPresenter( SingletonPresenter<T> presenter ) {
        this.presenter = presenter;
    }

    @UiHandler("cancelButton")
    public void onClickCancel(ClickEvent e) {
        places.goTo( showPlace( this.presenter.get() ) );
    }

    @UiHandler("reloadButton")
    public void onReloadClick(ClickEvent event) {
        presenter.reload();
    }
    
    @UiHandler("editButton")
    public void onClickEdit(ClickEvent e) {
        places.goTo( editPlace( this.presenter.get() ) );
    }

    @UiHandler("saveButton")
    public void onClickSave(ClickEvent e) {
        presenter.save( editorDriver.flush() );
    }

    private void setupButtons( boolean editable, T model ) {
        //reload.setVisible(true);
        editButton.setVisible( false );
        saveButton.setVisible( false );
        cancelButton.setVisible( false );
        editorDriver.edit( model );
        editor.setEnabled( editable );
    }

    public void edit( T model ) {
        setupButtons( true, model );        
        saveButton.setVisible( isAllowed( EDIT ) );
        cancelButton.setVisible( true );
    }

    public void show( T model ) {
        setupButtons( false, model );  
        editButton.setVisible( isAllowed( EDIT ) );
    }

    public void reset( T model ) {
        editorDriver.edit( model );
    }

    public boolean isDirty() {
        return editorDriver.isDirty();
    }

}