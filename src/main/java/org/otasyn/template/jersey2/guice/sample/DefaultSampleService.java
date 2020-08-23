package org.otasyn.template.jersey2.guice.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.otasyn.template.jersey2.guice.sample.AnotherService;
import org.otasyn.template.jersey2.guice.sample.SampleService;

@Singleton
public class DefaultSampleService implements SampleService {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultSampleService.class);

  @Inject AnotherService anotherService;

  @Override
  public String getMessage() {
    LOG.debug("Build the message.");

    return "Hi " + anotherService.provideName();
  }

}
