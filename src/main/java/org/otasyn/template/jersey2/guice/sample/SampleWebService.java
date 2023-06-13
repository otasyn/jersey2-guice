package org.otasyn.template.jersey2.guice.sample;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.otasyn.template.jersey2.guice.sample.SampleService;

@Path("/sample")
@Produces(MediaType.TEXT_PLAIN)
public class SampleWebService {

  private static final Logger LOG = LoggerFactory.getLogger(SampleWebService.class); 

  @Inject SampleService sampleService;

  @GET
  @RequiresPermissions("sample:read")
  public String get() {
    LOG.debug("Get a sample message.");

    return sampleService.getMessage();
  }

  @GET
  @Path("{message}")
  @RequiresPermissions("sample:create")
  public String getWithMessage(@PathParam("message") String message) {
    LOG.debug("Get a sample message from the path.");

    return "Path message: " + message;
  }

  @GET
  @Path("typesafe")
  @RequiresPermissions("sample:read")
  public String getWithTypesafeConfigMessage() {
    LOG.debug("Get a sample message from typesafe config.");

    return sampleService.getTypesafeConfigMessage();
  }
}
