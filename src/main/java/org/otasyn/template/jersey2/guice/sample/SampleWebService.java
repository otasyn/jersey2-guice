package org.otasyn.template.jersey2.guice.sample;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.otasyn.template.jersey2.guice.sample.SampleService;

@Path("/sample")
public class SampleWebService {

  @Inject SampleService sampleService;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String get() {
    return sampleService.getMessage();
  }

}