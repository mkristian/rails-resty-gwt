package <%= models_base_package %>;

public class <%= class_name %> {

<% for attribute in attributes -%>
<% if attribute.type == :has_one -%>
  <%= attribute.name.classify %> <%= attribute.name %>;
<% elsif attribute.type == :has_many -%>
  public java.util.List<<%= attribute.name.classify %>> <%= attribute.name %>;
<% else -%>
  public <%= type_map[attribute.type] || attribute.type.to_s.classify %> <%= attribute.name.classify.sub(/^(.)/){ $1.downcase } %>;
<% end -%>

<% end -%>
<% if options[:timestamps] %>
  //TODO timestamps

<% end -%>
<% if options[:modified_by] %>
  //TODO modified_by

<% end -%>
}