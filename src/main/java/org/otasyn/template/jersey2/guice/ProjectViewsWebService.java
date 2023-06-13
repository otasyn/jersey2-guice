package org.otasyn.template.jersey2.guice;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/views")
@Produces(MediaType.TEXT_HTML)
public class ProjectViewsWebService {

  private static final Logger LOG = LoggerFactory.getLogger(ProjectViewsWebService.class);

  @GET
  public Viewable root() {
    LOG.debug("Get root view.");

    Map<String, String> model = new HashMap<>();
    model.put("jspName", "root");

    return new Viewable("/root", model);
  }

  @GET
  @Path("/login")
  public Viewable login() {
    LOG.debug("get login view");

    return new Viewable("/login");
  }

  @POST
  @Path("/login")
  public Viewable loginPost() {
    LOG.debug("post login view");

    return new Viewable("/login");
  }

  @GET
  @Path("/restricted")
  @RequiresAuthentication
  public Viewable restricted() {
    LOG.debug("get restricted view");

    Map<String, String> model = new HashMap<>();
    model.put("jspName", "restricted");

    return new Viewable("/restricted", model);
  }

}
