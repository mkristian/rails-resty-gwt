package <%= models_package %>;

<% if options[:timestamps] %>
import java.util.Date;
<% end -%>

<% if !options[:singleton] || options[:timestamps] || options[:modified_by] -%>
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
<% end -%>
import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

import de.mkristian.gwt.rails.models.HasToDisplay;
<% unless options[:singleton] -%>
import de.mkristian.gwt.rails.models.Identifyable;
<% end -%>

@Json(style = Style.RAILS<% if class_name.downcase == class_name.underscore -%>)<% else -%>, name = "<%= class_name.underscore %>")<% end %>
public class <%= class_name %> implements HasToDisplay<% unless options[:singleton] %>, Identifyable<% end -%> {

<% unless options[:singleton] -%>
  public final int id;
<% end -%>
<% if options[:timestamps] -%>

  @Json(name = "created_at")
  private final Date createdAt;

  @Json(name = "updated_at")
  private final Date updatedAt;
<% end -%>
<% if options[:modified_by] -%>

  @Json(name = "modified_by")
  private final User modifiedBy;
<% end -%>
<% for attribute in attributes -%>
<% name = attribute.name.camelcase.sub(/^(.)/) {$1.downcase} -%>

<% if attribute.type == :belongs_to -%>
  @Json(name = "<%= name.underscore %>_id")
  private int <%= name %>Id;
<% end -%>
<% if name != name.underscore -%>  @Json(name = "<%= name.underscore %>")
<% end -%>
<% if attribute.type == :has_one || attribute.type == :belongs_to -%>
  private <%= attribute.name.classify %> <%= name %>;
<% elsif attribute.type == :has_many -%>
  private java.util.List<<%= attribute.name.singularize.classify %>> <%= name %>;
<% else -%>
  private <%= type_map[attribute.type] || attribute.type.to_s.classify %> <%= name %>;
<% end -%>
<% end -%>
<% if !options[:singleton] || options[:timestamps] || options[:modified_by] -%>

  public <%= class_name %>(){
    this(<% unless options[:singleton] -%>0<% if options[:timestamps] -%>, null, null<% if options[:modified_by] -%>, null<% end -%><% end -%><% else -%><% if options[:timestamps] -%>null, null<% if options[:modified_by] -%>, null<% end -%><% else -%><% if options[:modified_by] -%>null<% end -%><% end -%><% end -%><% for attribute in attributes -%><% if attribute.type == :belongs_to -%>, 0<% end -%><% end -%>);
  }
  
  @JsonCreator
  public <%= class_name %>(<% unless options[:singleton] -%>@JsonProperty("id") int id<% if options[:timestamps] -%>, 
          @JsonProperty("createdAt") Date createdAt, 
          @JsonProperty("updatedAt") Date updatedAt<% if options[:modified_by] -%>,
          @JsonProperty("modifiedBy") User modifiedBy<% end -%><% end -%><% else -%><% if options[:timestamps] -%>@JsonProperty("createdAt") Date createdAt, 
          @JsonProperty("updatedAt") Date updatedAt<% if options[:modified_by] -%>,
          @JsonProperty("modifiedBy") User modifiedBy<% end -%><% else -%><% if options[:modified_by] -%>@JsonProperty("modifiedBy") Date modifiedBy<% end -%><% end -%><% end -%><% for attribute in attributes -%><% if attribute.type == :belongs_to -%>,
<% name = attribute.name.camelcase.sub(/^(.)/) {$1.downcase} -%>
          @JsonProperty("<%= name %>Id") int <%= name %>Id<% end -%><% end -%>){
<% unless options[:singleton] -%>
    this.id = id;
<% end -%>
<% if options[:timestamps] -%>
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
<% end -%>
<% if options[:modified_by] -%>
    this.modifiedBy = modifiedBy;
<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
<% name = attribute.name.camelcase.sub(/^(.)/) {$1.downcase} -%>
    this.<%= name %>Id = <%= name %>Id;
<% end -%>
<% end -%>
  }
<% end -%>
<% unless options[:singleton] -%>

  public int getId(){
    return id;
  }
<% end -%>
<% if options[:timestamps] -%>

  public Date getCreatedAt(){
    return createdAt;
  }

  public Date getUpdatedAt(){
    return updatedAt;
  }
<% end -%>
<% if options[:modified_by] -%>

  public User getModifiedBy(){
    return modifiedBy;
  }
<% end -%>
<% for attribute in attributes -%>
<% name = attribute.name.camelcase.sub(/^(.)/) {$1.downcase} -%>

<% if attribute.type == :has_one || attribute.type == :belongs_to -%>
  public <%= attribute.name.classify %> get<%= attribute.name.camelcase %>(){
    return <%= name %>;
  }

  public void set<%= attribute.name.camelcase %>(<%= attribute.name.classify %> value){
    <%= name %> = value;
<% if attribute.type == :belongs_to -%>
    <%= name %>Id = value == null ? 0 : value.getId();
<% end -%>
  }
<% if attribute.type == :belongs_to -%>

  public int get<%= attribute.name.camelcase %>Id(){
    return <%= name %>Id;
  }
<% end -%>
<% elsif attribute.type == :has_many -%>
  public java.util.List<<%= attribute.name.singularize.classify %>> get<%= attribute.name.pluralize.camelcase %>(){
    return <%= name %>;
  }

  public void set<%= attribute.name.pluralize.camelcase %>(java.util.List<<%= attribute.name.singularize.classify %>> value){
    <%= name %> = value;
  }
<% else -%>
  public <%= type_map[attribute.type] || attribute.type.to_s.classify %> get<%= name.camelcase %>(){
    return <%= name %>;
  }

  public void set<%= attribute.name.camelcase %>(<%= type_map[attribute.type] || attribute.type.to_s.classify %> value){
    <%= name %> = value;
  }
<% end -%>
<% end -%>
<% if attributes.detect {|a| a.type == :belongs_to } -%>

  public <%= class_name %> minimalClone() {
      <%= class_name %> clone = new <%= class_name %>(<% unless options[:singleton] -%>id, <% end -%><% if options[:timestamps] -%>null, updatedAt, <% end -%><% if options[:modified_by] -%>null, <% end -%><%= attributes.select {|a| a.type == :belongs_to }.collect do |attribute|
  name = attribute.name.camelcase.sub(/^(.)/) {$1.downcase}
  "#{name}Id"
end.join ',' -%>);
<% attributes.select {|attr| ![:belongs_to, :has_one, :has_many].include?(attr.type) }.each do |attribute| -%>
<% name = attribute.name.camelcase.sub(/^(.)/) {$1.downcase} -%>
      clone.set<%= attribute.name.camelcase %>(this.<%= name %>);
<% end -%>
      return clone;
  }
<% end -%>
<% unless options[:singleton] -%>

  public int hashCode(){
    return id;
  }

  public boolean equals(Object other){
    return (other instanceof <%= class_name %>) && 
        ((<%= class_name %>)other).id == id;
  }
<% end -%>

  public String toDisplay() {
    return <%= attributes.first.name.camelcase.sub(/^(.)/) {$1.downcase} %><% unless attributes.first.type == :string %> + ""<% end -%>;
  }
}
