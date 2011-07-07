package <%= views_package %>;

<% if options[:timestamps] %>
import java.util.Date;
<% end -%>

import <%= gwt_rails_package %>.RestfulAction;
import <%= gwt_rails_package %>.RestfulActionEnum;

import <%= models_package %>.<%= class_name %>;
import <%= places_package %>.<%= class_name %>Place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
<% if options[:timestamps] %>
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
<% end -%>
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Singleton;

@Singleton
public class <%= class_name %>ViewImpl extends Composite
        implements <%= class_name %>View {

    @UiTemplate("<%= class_name %>View.ui.xml")
    interface <%= class_name %>ViewUiBinder extends UiBinder<Widget, <%= class_name %>ViewImpl> {}
    
    private static <%= class_name %>ViewUiBinder uiBinder = GWT.create(<%= class_name %>ViewUiBinder.class);


    @UiField
    Button newButton;
    
    @UiField
    Button createButton;
    
    @UiField
    Button editButton;

    @UiField
    Button saveButton;

    @UiField
    Button deleteButton;

<% unless options[:singleton] -%>
    @UiField
    Label id;

<% end -%>
<% if options[:timestamps] %>
    @UiField
    Label createdAt;

    @UiField
    Label updatedAt;

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

<% unless options[:singleton] -%>
    private int idCache;

<% end -%>
<% if options[:timestamps] %>
    private Date createdAtCache;
    private Date updatedAtCache;

<% end -%>
    public <%= class_name %>ViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("newButton")
    void onClickNew(ClickEvent e) {
        presenter.goTo(new <%= class_name %>Place(RestfulActionEnum.NEW));
    }

    @UiHandler("createButton")
    void onClickCreate(ClickEvent e) {
        presenter.create();
    }

    @UiHandler("editButton")
    void onClickEdit(ClickEvent e) {
        presenter.goTo(new <%= class_name %>Place(<% unless options[:singleton] -%>idCache, <% end -%>RestfulActionEnum.EDIT));
    }

    @UiHandler("saveButton")
    void onClickSave(ClickEvent e) {
        presenter.save();
    }

    @UiHandler("deleteButton")
    void onClickDelete(ClickEvent e) {
        presenter.delete();
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void reset(<%= class_name %> model) {
<% if options[:singleton] -%>
        throw new RuntimeException( "TODO singleton support");
<% else -%>
        if(model.id > 0){
            id.setText("id: " + model.id);
<% if options[:timestamps] %>
            createdAt.setText("created at: "
                    + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM).format(model.created_at));
            updatedAt.setText("updated at: "
                    + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM).format(model.updated_at));
<% end -%>
        }
        else {
            id.setText(null);
<% if options[:timestamps] %>
            createdAt.setText(null);
            updatedAt.setText(null);
<% end -%>
        }
        this.idCache = model.id;
<% if options[:timestamps] %>
        this.createdAtCache = model.created_at;
        this.updatedAtCache = model.updated_at;
<% end -%>
<% end -%>
<% for attribute in attributes -%>
<% if attribute.type != :has_one && attribute.type != :has_many -%>
        <%= attribute.name.classify.sub(/^(.)/){ $1.downcase } %>.setText(model.<%= attribute.name.classify.underscore.sub(/^(.)/){ $1.downcase } %>);
<% end -%>
<% end -%>
    }

    public void reset(RestfulAction action) {
        GWT.log(action.name() + " <%= class_name %>" + <% unless options[:singleton] -%>(idCache > 0 ? "(" + id + ")" : "")<% end -%>);
        newButton.setVisible(!action.name().equals(RestfulActionEnum.NEW.name()));
        createButton.setVisible(action.name().equals(RestfulActionEnum.NEW.name()));
        editButton.setVisible(action.name().equals(RestfulActionEnum.SHOW.name()));
        saveButton.setVisible(action.name().equals(RestfulActionEnum.EDIT.name()));
        deleteButton.setVisible(!action.name().equals(RestfulActionEnum.NEW.name()));
        setEnabled(!action.viewOnly());
    }

    public <%= class_name %> retrieve<%= class_name %>() {
        <%= class_name %> model = new <%= class_name %>();
<% unless options[:singleton] -%>
        model.id = idCache;
<% end -%>
<% if options[:timestamps] %>
        model.created_at = createdAtCache;
        model.updated_at = updatedAtCache;
<% end -%>

<% for attribute in attributes -%>
<% if attribute.type != :has_one && attribute.type != :has_many -%>
        model.<%= attribute.name.classify.underscore.sub(/^(.)/){ $1.downcase } %> = <%= attribute.name.classify.sub(/^(.)/){ $1.downcase } %>.getText();
<% end -%>

<% end -%>
        return model;
    }

    public void setEnabled(boolean enabled) {
<% for attribute in attributes -%>
<% if attribute.type != :has_one && attribute.type != :has_many -%>
         <%= attribute.name.classify.sub(/^(.)/){ $1.downcase } %>.setEnabled(enabled);
<% end -%>
<% end -%>
    }
}
