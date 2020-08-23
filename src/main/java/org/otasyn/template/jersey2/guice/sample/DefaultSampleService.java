package org.otasyn.template.jersey2.guice.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.otasyn.template.jersey2.guice.TypesafeConfig;
import org.otasyn.template.jersey2.guice.sample.AnotherService;
import org.otasyn.template.jersey2.guice.sample.SampleService;

@Singleton
public class DefaultSampleService implements SampleService {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultSampleService.class);

  private AnotherService anotherService;
  private Config sampleConfig;
  
  @Inject
  public DefaultSampleService(
      AnotherService anotherService,
      @TypesafeConfig Config config
  ) {
    this.anotherService = anotherService;
    this.sampleConfig = config.getConfig("public.sample");
  }

  @Override
  public String getMessage() {
    LOG.debug("Build the message.");

    return "Hi " + anotherService.provideName();
  }

  @Override
  public String getTypesafeConfigMessage() {
    LOG.debug("Retrieve the typesafe config message.");

    return sampleConfig.getString("message");
  }

}
