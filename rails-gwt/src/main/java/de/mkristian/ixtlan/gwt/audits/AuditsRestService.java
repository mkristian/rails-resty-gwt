package de.mkristian.ixtlan.gwt.audits;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import de.mkristian.gwt.rails.dispatchers.DefaultDispatcherSingleton;


@Options(dispatcher = DefaultDispatcherSingleton.class)
public interface AuditsRestService extends RestService {

  @GET @Path("/audits")
  void index(MethodCallback<List<Audit>> callback);

  @GET @Path("/audits/{id}")
  void show(@PathParam("id") int id, MethodCallback<Audit> callback);

}
