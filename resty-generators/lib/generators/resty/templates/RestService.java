package <%= rest_services_package %>;

<% if action_map.values.member? :get_all -%>
import java.util.List;
<% end -%>

import javax.ws.rs.*;

import org.fusesource.restygwt.client.*;

<% if name -%>
import <%= models_package %>.*;
<% end -%>

<% if options[:singleton] -%>@Path("/<%= table_name %>.json")<% end %>
//@Options(dispatcher = RestfulRetryingDispatcher.class)
public interface <%= controller_class_name %>RestService extends RestService {

<% actions.each do |action| 
     case action_map[action]
     when :get_all -%>
  @GET @Path("/<%= table_name %>.json")
  //@Options(dispatcher = RetryingDispatcher.class)
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
