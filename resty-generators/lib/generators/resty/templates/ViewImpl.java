package <%= views_package %>;

<% if options[:timestamps] %>
import java.util.Date;
<% end -%>
<% if options[:timestamps] -%>
import <%= gwt_rails_package %>.<% unless options[:singleton] -%>Identifyable<% end -%>TimestampedView;<% else -%><% unless options[:singleton] -%>
import <%= gwt_rails_package %>.IdentifyableView;<% end -%><% end -%>

import <%= gwt_rails_package %>.RestfulAction;
import <%= gwt_rails_package %>.RestfulActionEnum;

import <%= models_package %>.<%= class_name %>;
import <%= places_package %>.<%= class_name %>Place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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
  // TODO <%= attribute.name.classify %> <%= attribute.name %>;
<% elsif attribute.type == :has_many -%>
  // TODO public java.util.List<<%= attribute.name.classify %>> <%= attribute.name %>;
<% else -%>
    @UiField
    TextBox <%= attribute.name.classify.sub(/^(.)/){ $1.downcase } %>;
<% end -%>

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
        presenter.goTo(new <%= class_name %>Place(<% unless options[:singleton] -%>idCache, <% end -%>RestfulActionEnum.EDIT));
    }

    @UiHandler("saveButton")
    void onClickSave(ClickEvent e) {
        presenter.save();
    }
<% unless options[:singleton] -%>
    @UiHandler("deleteButton")
    void onClickDelete(ClickEvent e) {
        presenter.delete();
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
<%     name = attribute.name.classify.sub(/^(.)/){ $1.downcase } -%>
        <%= name %>.setText(model.<%= name %><% if type_conversion_map[attribute.type] -%> + ""<% end -%>);
<%   end -%>
<% end -%>
    }

    public void reset(RestfulAction action) {
        GWT.log(action.name() + " <%= class_name %>"<% unless options[:singleton] -%> + (idCache > 0 ? "(" + idCache + ")" : "")<% end -%>);
<% if options[:readonly] -%>
        setEnabled(false);
<% else -%>
<% unless options[:singleton] -%>
        newButton.setVisible(!action.name().equals(RestfulActionEnum.NEW.name()));
        createButton.setVisible(action.name().equals(RestfulActionEnum.NEW.name()));
<% end -%>
        editButton.setVisible(action.name().equals(RestfulActionEnum.SHOW.name()));
        saveButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
<% unless options[:singleton] -%>
        deleteButton.setVisible(!action.name().equals(RestfulActionEnum.NEW.name()));
<% end -%>
        setEnabled(!action.viewOnly());
<% end -%>
    }

    public <%= class_name %> retrieve<%= class_name %>() {
        <%= class_name %> model = new <%= class_name %>();
<% unless options[:singleton] -%>
        model.id = idCache;
<% end -%>
<% if options[:timestamps] %>
        model.createdAt = createdAtCache;
        model.updatedAt = updatedAtCache;
<% end -%>

<% for attribute in attributes -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.classify.sub(/^(.)/){ $1.downcase } -%>
        model.<%= name %> = <% if (conv = type_conversion_map[attribute.type]).nil? -%><%= name %>.getText()<% else -%><%= conv %>(<%= name %>.getText())<% end -%>;
<%   end -%>

<% end -%>
        return model;
    }

    public void setEnabled(boolean enabled) {
<% for attribute in attributes -%>
<%   if attribute.type != :has_one && attribute.type != :has_many -%>
<%     name = attribute.name.classify.sub(/^(.)/){ $1.downcase } -%>
         <%= name %>.setEnabled(enabled);
<%   end -%>
<% end -%>
    }
}
