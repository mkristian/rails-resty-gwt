package <%= models_package %>;

<% if options[:timestamps] %>
import java.util.Date;
<% end -%>

public class <%= class_name %> {

<% unless options[:singleton] -%>
  public int id;

<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :has_one -%>
  <%= attribute.name.classify %> <%= attribute.name %>;
<% elsif attribute.type == :has_many -%>
  public java.util.List<<%= attribute.name.classify %>> <%= attribute.name %>;
<% else -%>
  public <%= type_map[attribute.type] || attribute.type.to_s.classify %> <%= attribute.name.classify.underscore.sub(/^(.)/){ $1 } %>;
<% end -%>
<% end -%>
<% if options[:timestamps] %>
  public Date created_at;
  public Date updated_at;
<% end -%>
<% if options[:modified_by] %>
  public options[:modified_by].classify.underscore modified_by
<% end -%>
}