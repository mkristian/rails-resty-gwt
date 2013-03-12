package de.mkristian.ixtlan.gwt.errors;

import de.mkristian.gwt.rails.dispatchers.DefaultDispatcherSingleton;

import java.util.List;

import javax.ws.rs.*;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;


@Options(dispatcher = DefaultDispatcherSingleton.class)
public interface ErrorsRestService extends RestService {

  @GET @Path("/errors")
  void index(MethodCallback<List<Error>> callback);

  @GET @Path("/errors/{id}")
  void show(@PathParam("id") int id, MethodCallback<Error> callback);

}
