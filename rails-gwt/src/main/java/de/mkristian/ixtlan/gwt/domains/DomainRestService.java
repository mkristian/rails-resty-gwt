package de.mkristian.ixtlan.gwt.domains;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import de.mkristian.gwt.rails.dispatchers.DefaultDispatcherSingleton;


@Options(dispatcher = DefaultDispatcherSingleton.class)
public interface DomainRestService extends RestService {

  @GET @Path("/domains")
  void index(MethodCallback<List<Domain>> callback);

  @GET @Path("/domains/{id}")
  void show(@PathParam("id") int id, MethodCallback<Domain> callback);

}
