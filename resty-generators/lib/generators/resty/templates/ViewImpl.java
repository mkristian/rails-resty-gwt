package <%= views_package %>;

<% unless options[:singleton] -%>
import java.util.List;

<% end -%>
<% if options[:timestamps] -%>
import <%= gwt_rails_package %>.views.<% unless options[:singleton] -%>Identifyable<% end -%>TimestampedView;<% else -%><% unless options[:singleton] -%>
import <%= gwt_rails_package %>.views.IdentifyableView;<% end -%><% end -%>

import <%= gwt_rails_package %>.views.ModelButton;
import <%= gwt_rails_package %>.places.RestfulAction;
import <%= gwt_rails_package %>.places.RestfulActionEnum;

import <%= models_package %>.<%= class_name %>;
import <%= places_package %>.<%= class_name %>Place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
<% unless options[:singleton] -%>
import com.google.gwt.event.dom.client.ClickHandler;
<% end -%>
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Singleton;

@Singleton
public class <%= class_name %>ViewImpl extends <% if options[:timestamps] -%><% unless options[:singleton] -%>Identifyable<% end -%>TimestampedView<% else -%><% unless options[:singleton] -%>IdentifyableView<% else -%>Composite<% end -%><% end %>
        implements <%= class_name %>View {

    @UiTemplate("<%= class_name %>View.ui.xml")
    interface <%= class_name %>ViewUiBinder extends UiBinder<Widget, <%= class_name %>ViewImpl> {}
    
    private static <%= class_name %>ViewUiBinder uiBinder = GWT.create(<%= class_name %>ViewUiBinder.class);

<% unless options[:readonly] -%>
<% unless options[:singleton] -%>
    @UiField
    Button newButton;
    
    @UiField
    Button createButton;
<% end -%>    
    @UiField
    Button editButton;

    @UiField
    Button saveButton;
<% unless options[:singleton] -%>
    @UiField
    Button deleteButton;

<% end -%>
<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :has_one -%>
  // TODO <%= attribute.name.camelcase %> <%= attribute.name %>;
<% elsif attribute.type == :has_many -%>
  // TODO public java.util.List<<%= attribute.name.camelcase %>> <%= attribute.name %>;
<% else -%>
    @UiField
    TextBox <%= attribute.name.camelcase.sub(/^(.)/){ $1.downcase } %>;
<% end -%>

<% end -%>
    @UiField
    Panel form;
<% unless options[:singleton] -%>    
    @UiField
    FlexTable list;
<% end -%>

    private Presenter presenter;

    public <%= class_name %>ViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }
<% unless options[:readonly] %>
<% unless options[:singleton] -%>
    @UiHandler("newButton")
    void onClickNew(ClickEvent e) {
        presenter.goTo(new <%= class_name %>Place(RestfulActionEnum.NEW));
    }

    @UiHandler("createButton")
    void onClickCreate(ClickEvent e) {
        presenter.create();
    }

<% end -%>
    @UiHandler("editButton")
    void onClickEdit(ClickEvent e) {
        presenter.goTo(new <%= class_name %>Place(<% unless options[:singleton] -%>id.getValue(), <% end -%>RestfulActionEnum.EDIT));
    }

    @UiHandler("saveButton")
    void onClickSave(ClickEvent e) {
        presenter.save();
    }

<% unless options[:singleton] -%>
    @UiHandler("deleteButton")
    void onClickDelete(ClickEvent e) {
        presenter.delete(retrieve<%= class_name %>());
    }

<% end -%>
<% end -%>
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void reset(<%= class_name %> model) {
<% if options[:singleton] && options[:timestamps] -%>
        resetSignature(model.createdAt, model.updatedAt);
<% else -%>
<% if options[:timestamps] %>
        resetSignature(model.id, model.createdAt, model.updatedAt);
<% else -%>
        resetSignature(model.id);
<% end -%>
<% end -%>
<% for attribute in attributes -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.camelcase.sub(/^(.)/){ $1.downcase } -%>
        <%= name %>.setText(model.<%= name %><% if type_conversion_map[attribute.type] -%> + ""<% end -%>);
<%   end -%>
<% end -%>
    }

    public void reset(RestfulAction action) {
        newButton.setVisible(!action.name().equals(RestfulActionEnum.NEW.name()));
        if(action.name().equals(RestfulActionEnum.INDEX.name())){
            GWT.log(action.name() + " Account");
            editButton.setVisible(false);
            list.setVisible(true);
            form.setVisible(false);
        }
        else {
            GWT.log(action.name() + " <%= class_name %>"<% unless options[:singleton] -%> + (id.getValue() != null ? "(" + id.getValue() + ")" : "")<% end -%>);
<% if options[:readonly] -%>
            setEnabled(false);
<% else -%>
<% unless options[:singleton] -%>
            createButton.setVisible(action.name().equals(RestfulActionEnum.NEW.name()));
<% end -%>
            editButton.setVisible(action.name().equals(RestfulActionEnum.SHOW.name()));
            saveButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
<% unless options[:singleton] -%>
            deleteButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
<% end -%>
            setEnabled(!action.viewOnly());
            list.setVisible(false);
            form.setVisible(true);
<% end -%>
        }
    }

    public <%= class_name %> retrieve<%= class_name %>() {
        <%= class_name %> model = new <%= class_name %>();
<% unless options[:singleton] -%>
        model.id = id.getValue() == null ? 0 : id.getValue();
<% end -%>
<% if options[:timestamps] %>
        model.createdAt = createdAt.getValue();
        model.updatedAt = updatedAt.getValue();
<% end -%>

<% for attribute in attributes -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.camelcase.sub(/^(.)/){ $1.downcase } -%>
        model.<%= name %> = <% if (conv = type_conversion_map[attribute.type]).nil? -%><%= name %>.getText()<% else -%><%= conv %>(<%= name %>.getText())<% end -%>;
<%   end -%>

<% end -%>
        return model;
    }

    public void setEnabled(boolean enabled) {
<% for attribute in attributes -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.camelcase.sub(/^(.)/){ $1.downcase } -%>
         <%= name %>.setEnabled(enabled);
<%   end -%>
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

    private Button newButton(RestfulActionEnum action, Account model){
        ModelButton<Account> button = new ModelButton<Account>(action, model);
        button.addClickHandler(clickHandler);
        return button;
    }

    public void reset(List<Account> models) {
        list.removeAllRows();
        list.setText(0, 0, "Id");
<% attributes.each_with_index do |attr, index| -%>
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
        list.setText(row, 0, model.id + "");
<% attributes.each_with_index do |attribute, index| -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.camelcase.sub(/^(.)/){ $1.downcase } -%>
        list.setText(row, <%= index + 1 %>, model.<%= name %>);
<%   end -%>

<% end -%>
        list.setWidget(row, <%= attributes.size + 1 %>, newButton(RestfulActionEnum.SHOW, model));
        list.setWidget(row, <%= attributes.size + 2 %>, newButton(RestfulActionEnum.EDIT, model));
        list.setWidget(row, <%= attributes.size + 3 %>, newButton(RestfulActionEnum.DESTROY, model));
    }

    public void update(<%= class_name %> model) {
        String id = model.id + "";
        for(int i = 0; i < list.getRowCount(); i++){
            if(list.getText(i, 0).equals(id)){
                setRow(i, model);
                return;
            }
        }
    }

    public void remove(<%= class_name %> model) {
        String id = model.id + "";
        for(int i = 0; i < list.getRowCount(); i++){
            if(list.getText(i, 0).equals(id)){
                list.removeRow(i);
                return;
            }
        }
    }

    public void add(<%= class_name %> model) {
        setRow(list.getRowCount(), model);
    }
<% end -%>
}
