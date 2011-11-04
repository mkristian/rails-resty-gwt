package <%= views_package %>;

<% if !options[:singleton] || attributes.detect { |a| a.type == :belongs_to} -%>
import java.util.List;

<% end -%>
<% unless options[:read_only] -%>
import javax.inject.Inject;
<% end -%>

import <%= editors_package %>.<%= class_name %>Editor;
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= models_package %>.<%= attribute.name.classify %>;
<% end -%>
<% end -%>
<% unless options[:read_only] -%>
<% unless class_name == 'User' -%>
import <%= models_package %>.User;
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
<% unless options[:read_only] -%>
import com.google.gwt.uibinder.client.UiHandler;
<% end -%>
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
import static <%= gwt_rails_package %>.places.RestfulActionEnum.*;
<% unless options[:read_only] -%>
import <%= gwt_rails_package %>.session.SessionManager;
<% end -%>
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

<% unless options[:read_only] -%>
<% unless options[:singleton] -%>
    @UiField Button newButton;
<% end -%>
    @UiField Button editButton;
    @UiField Button showButton;

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
<% unless options[:read_only] -%>

    private final SessionManager<User> session;
<% end -%>

    public <%= class_name %>ViewImpl() {
<% unless options[:read_only] -%>
        this(null);
    }

    @Inject
    public <%= class_name %>ViewImpl(SessionManager<User> session) {
<% end -%>
        initWidget(BINDER.createAndBindUi(this));
        editorDriver.initialize(editor);
<% unless options[:read_only] -%>
        this.session = session;
<% end -%>
    }
<% unless options[:read_only] -%>

    private boolean isAllowed(RestfulActionEnum action){
        return session == null || session.isAllowed(<%= class_name %>Place.NAME, action);
    }
<% end -%>
<% unless options[:read_only] -%>
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

    public void setup(Presenter presenter, RestfulAction a) {
        RestfulActionEnum action = RestfulActionEnum.valueOf(a);
        this.presenter = presenter;
<% if options[:singleton] -%>
        editButton.setVisible((action == SHOW || action == INDEX) && isAllowed(EDIT));
        saveButton.setVisible(action == EDIT);
        showButton.setVisible(action == EDIT);
        editor.setEnabled(!action.viewOnly());
<% else -%>
<% unless options[:read_only] -%>
        newButton.setVisible(action != NEW && isAllowed(NEW));
<% end -%>
        if(action == INDEX){
<% unless options[:read_only] -%>
            editButton.setVisible(false);
            showButton.setVisible(false);
<% end -%>
            list.setVisible(true);
            model.setVisible(false);
        }
        else {
<% unless options[:read_only] -%>
            createButton.setVisible(action == NEW);
            editButton.setVisible(action == SHOW && isAllowed(EDIT));
            showButton.setVisible(action == EDIT);
            saveButton.setVisible(action == EDIT);
            deleteButton.setVisible(action == EDIT && isAllowed(DESTROY));
<% end -%>
            list.setVisible(false);
            model.setVisible(true);
        }
        editor.setEnabled(!action.viewOnly());
<% end -%>
    }

    public void edit(<%= class_name %> model) {
        this.editorDriver.edit(model);
        this.editor.resetVisibility();
    }

    public <%= class_name %> flush() {
        return editorDriver.flush();
    }
<% unless options[:singleton] -%>

    private final ClickHandler clickHandler = new ClickHandler() {
        
        @SuppressWarnings("unchecked")
        public void onClick(ClickEvent event) {
            ModelButton<<%= class_name %>> button = (ModelButton<<%= class_name %>>)event.getSource();
            switch(button.action){
<% unless options[:read_only] -%>
                case DESTROY:
                    presenter.delete(button.model);
                    break;
<% end -%>
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
<% index = 0 -%>
<% attributes.each do |attribute| -%>
<%   if !(attribute.type == :text && options[:read_only]) -%>
<%     index = index + 1 -%>
        list.setText(0, <%= index %>, "<%= attribute.name.humanize -%>");
<%   end -%>
<% end -%>
        list.getRowFormatter().addStyleName(0, "gwt-rails-model-list-header");
        int row = 1;
        for(<%= class_name %> model: models){
            setRow(row, model);
            row++;
        }
    }

    private void setRow(int row, <%= class_name %> model) {
        list.setText(row, 0, model.getId() + "");
<% index = 0 -%>
<% attributes.each do |attribute| -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.camelcase.sub(/^(.)/){ $1.downcase } -%>
<%     if !(attribute.type == :text && options[:read_only]) -%>
<%       index = index + 1 -%>
        list.setText(row, <%= index %>, model.get<%= name.camelcase %>()<%= attribute.type == :has_one || attribute.type == :belongs_to ? ' == null ? "-" : model.get' + name.camelcase + '().toDisplay()' : ' + ""' %>);
<%     end -%>
<%   end -%>
<% end -%>

        list.setWidget(row, <%= index + 1 %>, newButton(RestfulActionEnum.SHOW, model));
<% unless options[:read_only] -%>
        list.setWidget(row, <%= index + 2 %>, newButton(RestfulActionEnum.EDIT, model));
        list.setWidget(row, <%= index + 3 %>, newButton(RestfulActionEnum.DESTROY, model));
<% end -%>
    }
<% unless options[:read_only] -%>

    public void removeFromList(<%= class_name %> model) {
        String id = model.getId() + "";
        for(int i = 0; i < list.getRowCount(); i++){
            if(list.getText(i, 0).equals(id)){
                list.removeRow(i);
                return;
            }
        }
    }
<% end -%>
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
