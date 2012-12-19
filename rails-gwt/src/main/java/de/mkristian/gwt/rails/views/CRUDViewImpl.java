package de.mkristian.gwt.rails.views;

import static de.mkristian.gwt.rails.places.RestfulActionEnum.DESTROY;
import static de.mkristian.gwt.rails.places.RestfulActionEnum.EDIT;
import static de.mkristian.gwt.rails.places.RestfulActionEnum.NEW;

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

import de.mkristian.gwt.rails.editors.IdentifiableEditor;
import de.mkristian.gwt.rails.models.Identifyable;
import de.mkristian.gwt.rails.places.RestfulActionEnum;
import de.mkristian.gwt.rails.presenters.CRUDPresenter;
import de.mkristian.gwt.rails.session.Guard;

public abstract class CRUDViewImpl<T extends Identifyable> extends Composite {

    @UiField public Button newButton;
    @UiField public Button editButton;
    @UiField public Button cancelButton;
    @UiField public Button createButton;
    @UiField public Button saveButton;
    @UiField public Button deleteButton;

    @UiField(provided = true) public Label header;
    @UiField(provided = true) public IdentifiableEditor<T> editor;
    
    private CRUDPresenter<T> presenter;
    
    protected final Guard guard;
    protected final PlaceController places;
    protected final SimpleBeanEditorDriver<T, Editor<T>> editorDriver;

    public CRUDViewImpl( final String title,
            final Guard guard,
            final PlaceController places,
            final IdentifiableEditor<T> editor,
            final SimpleBeanEditorDriver<T, Editor<T>> driver) {
        this.header = new Label( title );
        this.guard = guard;
        this.places = places;     
        this.editor = editor;
        this.editorDriver = driver;
        this.editorDriver.initialize( editor );
    }

    protected abstract String placeName();
    protected abstract Place newPlace();
    protected abstract Place showPlace( T model );
    protected abstract Place editPlace( T model );
    protected abstract T newModel();

    protected boolean isAllowed( RestfulActionEnum action ){
        return guard.isAllowed( placeName(), action );
    }
    
    public void setPresenter(CRUDPresenter<T> presenter) {
        this.presenter = presenter;
    }

    @UiHandler("newButton")
    public void onClickNew(ClickEvent e) {
        places.goTo( newPlace() );
    }
    
    @UiHandler("cancelButton")
    public void onClickCancel(ClickEvent e) {
        places.goTo( showPlace( presenter.get() ) );
    }

    @UiHandler("reloadButton")
    public void onReloadClick(ClickEvent event) {
        presenter.reload();
    }
    
    @UiHandler("editButton")
    public void onClickEdit(ClickEvent e) {
        places.goTo( editPlace( presenter.get() ) );
    }

    @UiHandler("createButton")
    public void onClickCreate(ClickEvent e) {
        presenter.create( editorDriver.flush() );
    }

    @UiHandler("saveButton")
    public void onClickSave(ClickEvent e) {
        presenter.save( editorDriver.flush() );
    }

    @UiHandler("deleteButton")
    public void onClickDelete(ClickEvent e) {
        presenter.delete( editorDriver.flush() );
    }

    private void setupButtons(boolean editable, T model) {
        newButton.setVisible( true );
        createButton.setVisible( false );
        editButton.setVisible( false );
        saveButton.setVisible( false );
        cancelButton.setVisible( false );
        deleteButton.setVisible( false );
        editorDriver.edit( model );
        editor.setEnabled( editable );
    }

    public void edit(T model) {
        setupButtons( true, model );        
        newButton.setVisible( isAllowed( NEW ) );
        saveButton.setVisible( isAllowed( EDIT ) );
        cancelButton.setVisible( true );
        deleteButton.setVisible( isAllowed( DESTROY ) );   
    }

    public void show(T model) {
        setupButtons( false, model );  
        newButton.setVisible( isAllowed( NEW ) );
        editButton.setVisible( isAllowed( EDIT ) );
    }

    public void showNew() {
        setupButtons( true, newModel() );  
        createButton.setVisible( isAllowed( NEW ) );
    }

    public void reset(T model) {
        editorDriver.edit( model );
    }

    public boolean isDirty() {
        return editorDriver.isDirty();
    }
}