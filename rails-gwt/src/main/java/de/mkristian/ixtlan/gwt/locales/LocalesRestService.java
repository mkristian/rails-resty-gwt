package de.mkristian.ixtlan.gwt.locales;

import de.mkristian.gwt.rails.dispatchers.DefaultDispatcherSingleton;

import java.util.List;

import javax.ws.rs.*;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;


@Options(dispatcher = DefaultDispatcherSingleton.class)
public interface LocalesRestService extends RestService {

  @GET @Path("/locales")
  void index(MethodCallback<List<Locale>> callback);

  @GET @Path("/locales/{id}")
  void show(@PathParam("id") int id, MethodCallback<Locale> callback);

}
