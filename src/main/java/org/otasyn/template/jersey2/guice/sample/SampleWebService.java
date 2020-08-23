package org.otasyn.template.jersey2.guice.sample;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.otasyn.template.jersey2.guice.sample.SampleService;

@Path("/sample")
public class SampleWebService {

  private static final Logger LOG = LoggerFactory.getLogger(SampleWebService.class); 

  @Inject SampleService sampleService;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String get() {
    LOG.debug("Get a sample message.");

    return sampleService.getMessage();
  }

}
