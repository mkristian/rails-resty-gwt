package <%= models_package %>;

<% if options[:timestamps] %>
import java.util.Date;
<% end -%>

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

@Json(style = Style.RAILS<% if class_name.downcase == class_name.underscore -%>)<% else -%>, name = "<%= class_name.underscore %>")<% end %>
public class <%= class_name %> {

<% unless options[:singleton] -%>
  public int id;
<% end -%>
<% for attribute in attributes -%>
<% name = attribute.name.camelcase.sub(/^(.)/) {$1.downcase} -%>

<% if name != name.underscore -%>  @Json(name = "<%= name.underscore %>")
<% end -%>
<% if attribute.type == :has_one -%>
  public <%= attribute.name.camelcase %> <%= name %>;
<% elsif attribute.type == :has_many -%>
  public java.util.List<<%= attribute.name.classify %>> <%= name %>;
<% else -%>
  public <%= type_map[attribute.type] || attribute.type.to_s.classify %> <%= name %>;
<% end -%>
<% end -%>
<% if options[:timestamps] %>

  @Json(name = "created_at")
  public Date createdAt;

  @Json(name = "updated_at")
  public Date updatedAt;
<% end -%>
<% if options[:modified_by] %>

  @Json(name = "modified_by")
  public options[:modified_by].classify.underscore modifiedBy
<% end -%>
}