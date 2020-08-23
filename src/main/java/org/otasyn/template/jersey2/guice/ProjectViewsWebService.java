package org.otasyn.template.jersey2.guice;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/views")
public class ProjectViewsWebService {

  private static final Logger LOG = LoggerFactory.getLogger(ProjectViewsWebService.class);

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Viewable root() {
    LOG.debug("Get root view.");

    Map<String, String> model = new HashMap<>();
    model.put("jspName", "root");

    return new Viewable("/root", model);
  }

}
