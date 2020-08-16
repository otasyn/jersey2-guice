package org.otasyn.template.jersey2.guice;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

@Path("/")
public class ProjectWebService {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String root() {
    return "WebService#root()";
  }

  @GET
  @Path("test")
  public Viewable test() {
    Map<String, String> model = new HashMap<>();
    model.put("hello", "Hello");
    model.put("world", "World");

    return new Viewable("/test", model);
  }
}
