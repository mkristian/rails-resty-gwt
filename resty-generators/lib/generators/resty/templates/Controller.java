package <%= controllers_base_package %>;

<% if action_map.values.member? :get_all -%>
import java.util.List;
<% end -%>

import javax.ws.rs.*;

import org.fusesource.restygwt.client.*;

<% if name -%>
import <%= models_base_package %>.*;
<% end -%>

@Path("/<%= table_name %><%= options[:singleton] ? '.json' : '' %>")
public interface <%= controller_class_name %>Controller extends RestService {

<% actions.each do |action| 
     case action_map[action]
     when :get_all -%>
  @GET @Path("/<%= table_name %>.json")
  void <%= action %>(MethodCallback<List<<%= class_name %>>> callback);

//  @GET @Path("/<%= table_name %>.json")
//  void <%= action %>(MethodCallback<List<<%= class_name %>>> callback, @QueryParam("limit") int limit, @QueryParam("offset") int offset);
//
<%   when :get_single -%>
  @GET<% unless options[:singleton] -%> @Path("/<%= table_name %>/{id}.json")<% end %>
  void <%= action %>(<% unless options[:singleton] -%>@PathParam("id") int id, <% end -%>MethodCallback<<%= class_name %>> callback);

<%   when :post -%>
  @POST @Path("/<%= table_name %>.json")
  void <%= action %>(<%= class_name %> value, MethodCallback<<%= class_name %>> callback);

<%   when :put -%>
  @PUT<% unless options[:singleton] -%> @Path("/<%= table_name %>/{id}.json")<% end %>
  void <%= action %>(<% unless options[:singleton] -%>@PathParam("id") @Attribute("id") <% end -%><%= class_name %> value, MethodCallback<<%= class_name %>> callback);

<%   when :delete -%>
  @DELETE @Path("/<%= table_name %>/{id}.json")
  void <%= action %>(@PathParam("id") @Attribute("id") <%= class_name %> value, MethodCallback<Void> callback);

<%   else -%>
  @GET @Path("/<%= table_name %>/<%= action %>.json")
  void <%= action %>(MethodCallback<Void> callback);

<% end
end -%>
}