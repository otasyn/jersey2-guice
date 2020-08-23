package org.otasyn.template.jersey2.guice.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.otasyn.template.jersey2.guice.sample.AnotherService;

public class DefaultAnotherService implements AnotherService {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultAnotherService.class);

  @Override
  public String provideName() {
    LOG.debug("Provide name of this service.");

    return "from another service";
  }

}
