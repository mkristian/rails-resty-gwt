package <%= editors_package %>;

<% if attributes.detect {|a| a.type == :belongs_to } -%>
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= models_package %>.<%= attribute.name.classify %>;
<% end -%>
<% end -%>
import <%= models_package %>.<%= class_name %>;
<% if options[:modified_by] -%>
import <%= models_package %>.User;
<% end -%>

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
<% if options[:timestamps] -%>
import com.google.gwt.user.client.ui.DateLabel;
<% end -%>
<% if !options[:singleton] || options[:timestamps] || options[:modified_by] -%>
import com.google.gwt.user.client.ui.FlowPanel;
<% end -%>
<% unless options[:singleton] -%>
import com.google.gwt.user.client.ui.NumberLabel;
<% end -%>
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.mkristian.gwt.rails.editors.UserLabel;
import de.mkristian.gwt.rails.editors.DoubleBox;
import de.mkristian.gwt.rails.editors.IntegerBox;
import de.mkristian.gwt.rails.editors.LongBox;
import de.mkristian.gwt.rails.editors.IdentifyableListBox;

public class <%= class_name %>Editor extends Composite implements Editor<<%= class_name %>>{
    
    interface Binder extends UiBinder<Widget, <%= class_name %>Editor> {}

    private static final Binder BINDER = GWT.create(Binder.class);
    
<% if !options[:singleton] || options[:timestamps] || options[:modified_by] -%>
    @Ignore @UiField FlowPanel signature;

<% end -%>
<% unless options[:singleton] -%>
    @UiField public NumberLabel<Integer> id;
<% end -%>
<% if options[:timestamps] -%>
    @UiField DateLabel createdAt;
    @UiField DateLabel updatedAt;
<% end -%>
<% if options[:modified_by] -%>
    @UiField UserLabel<User> modifiedBy;
<% end -%>

<% for attribute in attributes -%>
<% if attribute.type == :has_one -%>
  // just display them
  // TODO <%= attribute.name.camelcase %> <%= attribute.name %>;
<% elsif attribute.type == :has_many -%>
  // just display them
  // TODO public java.util.List<<%= attribute.name.camelcase %>> <%= attribute.name %>;
<% elsif attribute.type == :belongs_to -%>
    @UiField IdentifyableListBox<<%= attribute.name.classify %>> <%= attribute.name.camelcase.sub(/^(.)/){ $1.downcase } %>;
<% else -%>
    @UiField <%= type_widget_map[attribute.type][2..-1] %> <%= attribute.name.camelcase.sub(/^(.)/){ $1.downcase } %>;
<% end -%>

<% end -%>
    public <%= class_name %>Editor() {
        initWidget(BINDER.createAndBindUi(this));
    }

<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
<% clazz = attribute.name.classify -%>
    public void reset<%= clazz.to_s.pluralize %>(List<<%= clazz %>> models){
        if(models == null){
            <%= clazz %> model = new <%= clazz %>() {
                public String toDisplay() { return "loading..."; }
            };
            <%= attribute.name.camelcase.sub(/^(.)/){ $1.downcase } %>.setAcceptableValues(Arrays.asList(model));
        }
        else{
            <%= clazz %> model = new <%= clazz %>() {
                public String toDisplay() { return "please select..."; }
            };
            List<<%= clazz %>> list = new ArrayList<<%= clazz %>>();
            list.add(model);
            list.addAll(models);
            <%= attribute.name.camelcase.sub(/^(.)/){ $1.downcase } %>.setAcceptableValues(list);
        }
    }

<% end -%>
<% end -%>
    public void resetVisibility() {
<% if !options[:singleton] -%>
        this.signature.setVisible(id.getValue() != null && id.getValue() > 0);
<% elsif options[:timestamps] -%>
        this.signature.setVisible(createdAt.getValue() != null);
<% elsif options[:modified_by] -%>
       this.signature.setVisible(modifiedBy.getValue() != null);
<% end -%>
    }

    public void setEnabled(boolean enabled) {
        resetVisibility();
<% for attribute in attributes -%>
<% if attribute.type == :has_one -%>
  // TODO <%= attribute.name.camelcase %> <%= attribute.name %>;
<% elsif attribute.type == :has_many -%>
  // TODO public java.util.List<<%= attribute.name.camelcase %>> <%= attribute.name %>;
<% else -%>
        this.<%= attribute.name.camelcase.sub(/^(.)/){ $1.downcase } %>.setEnabled(enabled);
<% end -%>
<% end -%>
    }
}