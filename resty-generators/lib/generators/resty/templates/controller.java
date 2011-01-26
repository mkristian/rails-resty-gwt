package <%= controllers_base_package %>;

<% if action_map.values.member? :get_all -%>
import java.util.List;
<% end -%>

import javax.ws.rs.*;

import org.fusesource.restygwt.examples.client.*;

<% if name -%>
import <%= models_base_package %>.<%= class_name %>;
<% end -%>

@Path("/<%= table_name %>")
public interface <%= controller_class_name %>Controller extends RestService {

<% actions.each do |action| 
     case action_map[action]
     when :get_all -%>
  @GET 
  void <%= action %>(MethodCallback<List<<%= class_name %>>> callback);

//  @GET 
//  void <%= action %>(MethodCallback<List<<%= class_name %>>> callback, @QueryParam("limit") int limit, @QueryParam("offset") int offset);
//
<%   when :get_single -%>
  @GET @Path("/{id}")
  void <%= action %>(@PathParam("id") int id, MethodCallback<<%= class_name %>> callback);

<%   when :post -%>
  @POST
  void <%= action %>(<%= class_name %> value, MethodCallback<<%= class_name %>> callback);

<%   when :put -%>
  @PUT @Path("/{id}")
  void <%= action %>(@PathParam("id") @Attribute("id") <%= class_name %> value, MethodCallback<<%= class_name %>> callback);

<%   when :delete -%>
  @DELETE @Path("/{id}")
  void <%= action %>(@PathParam("id") @Attribute("id") <%= class_name %> value, MethodCallback<Void> callback);

<%   else -%>
  @GET 
  void <%= action %>(MethodCallback<Void> callback);

<% end
end -%>
}