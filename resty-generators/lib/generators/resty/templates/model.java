package <%= models_base_package %>;

public class <%= class_name %> {
<% for attribute in attributes -%>

  <%= type_map[attribute.type] || attribute.type %> <%= attribute.name %>;
<% end -%>
<% if options[:timestamps] %>

  //TODO timestamps
<% end -%>

}