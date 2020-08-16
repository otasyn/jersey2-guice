package org.otasyn.template.jersey2.guice;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

@Path("/views")
public class ProjectViewsWebService {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Viewable root() {
    Map<String, String> model = new HashMap<>();
    model.put("jspName", "root");

    return new Viewable("/root", model);
  }

}
