package <%= views_package %>;

<% if !options[:singleton] || attributes.detect { |a| a.type == :belongs_to} -%>
import java.util.List;

<% end -%>
import <%= editors_package %>.<%= class_name %>Editor;
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= models_package %>.<%= attribute.name.classify %>;
<% end -%>
<% end -%>
import <%= models_package %>.<%= class_name %>;
import <%= places_package %>.<%= class_name %>Place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
<% unless options[:singleton] -%>
import com.google.gwt.event.dom.client.ClickHandler;
<% end -%>
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
<% unless options[:singleton] -%>
import com.google.gwt.user.client.ui.FlexTable;
<% end -%>
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import <%= gwt_rails_package %>.places.RestfulAction;
import <%= gwt_rails_package %>.places.RestfulActionEnum;
<% unless options[:singleton] -%>
import <%= gwt_rails_package %>.views.ModelButton;
<% end -%>

@Singleton
public class <%= class_name %>ViewImpl extends Composite implements <%= class_name %>View {

    @UiTemplate("<%= class_name %>View.ui.xml")
    interface Binder extends UiBinder<Widget, <%= class_name %>ViewImpl> {}
    
    private static Binder BINDER = GWT.create(Binder.class);

    interface EditorDriver extends SimpleBeanEditorDriver<<%= class_name %>, <%= class_name %>Editor> {}

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

<% unless options[:readonly] -%>
<% unless options[:singleton] -%>
    @UiField Button newButton;
<% end -%>
    @UiField Button editButton;
<% end -%>
    @UiField Button showButton;

<% unless options[:readonly] -%>
<% unless options[:singleton] -%>
    @UiField Button createButton;
<% end -%>
    @UiField Button saveButton;
<% unless options[:singleton] -%>
    @UiField Button deleteButton;
<% end -%>
<% end -%>

    @UiField Panel model;
<% unless options[:singleton] -%>
    @UiField FlexTable list;
<% end -%>

    @UiField <%= class_name %>Editor editor;

    private Presenter presenter;

    public <%= class_name %>ViewImpl() {
        initWidget(BINDER.createAndBindUi(this));
        editorDriver.initialize(editor);
    }
<% unless options[:readonly] -%>
<% unless options[:singleton] -%>

    @UiHandler("newButton")
    void onClickNew(ClickEvent e) {
        presenter.goTo(new <%= class_name %>Place(RestfulActionEnum.NEW));
    }
<% end -%>

    @UiHandler("showButton")
    void onClickShow(ClickEvent e) {
        presenter.goTo(new <%= class_name %>Place(<% unless options[:singleton] -%>editor.id.getValue(), <% end -%>RestfulActionEnum.SHOW));
    }

    @UiHandler("editButton")
    void onClickEdit(ClickEvent e) {
        presenter.goTo(new <%= class_name %>Place(<% unless options[:singleton] -%>editor.id.getValue(), <% end -%>RestfulActionEnum.EDIT));
    }
<% unless options[:singleton] -%>

    @UiHandler("createButton")
    void onClickCreate(ClickEvent e) {
        presenter.create();
    }
<% end -%>

    @UiHandler("saveButton")
    void onClickSave(ClickEvent e) {
        presenter.save();
    }
<% unless options[:singleton] -%>

    @UiHandler("deleteButton")
    void onClickDelete(ClickEvent e) {
        presenter.delete(flush());
    }

<% end -%>
<% end -%>
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void edit(<%= class_name %> model) {
        this.editorDriver.edit(model);
    }

    public <%= class_name %> flush() {
        return editorDriver.flush();
    }

    public void setEnabled(boolean enabled) {
        editor.setEnabled(enabled);
    }

    public void reset(RestfulAction action) {
<% if options[:singleton] -%>
        editButton.setVisible(action.name().equals(RestfulActionEnum.SHOW.name()) || 
                action.name().equals(RestfulActionEnum.INDEX.name()));
        saveButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
        showButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
        setEnabled(!action.viewOnly());
<% else -%>
        newButton.setVisible(!action.name().equals(RestfulActionEnum.NEW.name()));
        if(action.name().equals(RestfulActionEnum.INDEX.name())){
            editButton.setVisible(false);
            showButton.setVisible(false);
            list.setVisible(true);
            model.setVisible(false);
        }
        else {
<% if options[:readonly] -%>
            setEnabled(false);
<% else -%>
            createButton.setVisible(action.name().equals(RestfulActionEnum.NEW.name()));
            editButton.setVisible(action.name().equals(RestfulActionEnum.SHOW.name()));
            showButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
            saveButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
            deleteButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
<% end -%>
            setEnabled(!action.viewOnly());
            list.setVisible(false);
            model.setVisible(true);
        }
<% end -%>
    }
<% unless options[:singleton] -%>

    private final ClickHandler clickHandler = new ClickHandler() {
        
        @SuppressWarnings("unchecked")
        public void onClick(ClickEvent event) {
            ModelButton<<%= class_name %>> button = (ModelButton<<%= class_name %>>)event.getSource();
            switch(button.action){
                case DESTROY:
                    presenter.delete(button.model);
                    break;
                default:
                    presenter.goTo(new <%= class_name %>Place(button.model, button.action));
            }
        }
    };

    private Button newButton(RestfulActionEnum action, <%= class_name %> model){
        ModelButton<<%= class_name %>> button = new ModelButton<<%= class_name %>>(action, model);
        button.addClickHandler(clickHandler);
        return button;
    }

    public void reset(List<<%= class_name %>> models) {
        list.removeAllRows();
        list.setText(0, 0, "Id");
<% attributes.each_with_index do |attribute, index| -%>
        list.setText(0, <%= index + 1 %>, "<%= attribute.name.humanize -%>");
<% end -%>
        list.getRowFormatter().addStyleName(0, "model-list-header");
        int row = 1;
        for(<%= class_name %> model: models){
            setRow(row, model);
            row++;
        }
    }

    private void setRow(int row, <%= class_name %> model) {
        list.setText(row, 0, model.getId() + "");
<% attributes.each_with_index do |attribute, index| -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.camelcase.sub(/^(.)/){ $1.downcase } -%>
        list.setText(row, <%= index + 1 %>, model.get<%= name.camelcase %>()<%= attribute.type == :has_one || attribute.type == :belongs_to ? ' == null ? "-" : model.get' + name.camelcase + '().toDisplay()' : ' + ""' %>);
<%   end -%>

<% end -%>
        list.setWidget(row, <%= attributes.size + 1 %>, newButton(RestfulActionEnum.SHOW, model));
        list.setWidget(row, <%= attributes.size + 2 %>, newButton(RestfulActionEnum.EDIT, model));
        list.setWidget(row, <%= attributes.size + 3 %>, newButton(RestfulActionEnum.DESTROY, model));
    }

    public void updateInList(<%= class_name %> model) {
        String id = model.getId() + "";
        for(int i = 0; i < list.getRowCount(); i++){
            if(list.getText(i, 0).equals(id)){
                setRow(i, model);
                return;
            }
        }
    }

    public void removeFromList(<%= class_name %> model) {
        String id = model.getId() + "";
        for(int i = 0; i < list.getRowCount(); i++){
            if(list.getText(i, 0).equals(id)){
                list.removeRow(i);
                return;
            }
        }
    }

    public void addToList(<%= class_name %> model) {
        setRow(list.getRowCount(), model);
    }
<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
<% clazz = attribute.name.classify -%>

    public void reset<%= clazz.to_s.pluralize %>(List<<%= clazz %>> list){
        editor.reset<%= clazz.to_s.pluralize %>(list);
    }
<% end -%>
<% end -%>
}
