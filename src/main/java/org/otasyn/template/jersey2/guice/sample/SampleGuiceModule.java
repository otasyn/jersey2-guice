package org.otasyn.template.jersey2.guice.sample;

import com.google.inject.AbstractModule;

import org.otasyn.template.jersey2.guice.sample.AnotherService;
import org.otasyn.template.jersey2.guice.sample.DefaultAnotherService;
import org.otasyn.template.jersey2.guice.sample.DefaultSampleService;
import org.otasyn.template.jersey2.guice.sample.SampleService;

public class SampleGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AnotherService.class).to(DefaultAnotherService.class);
    bind(SampleService.class).to(DefaultSampleService.class);
  }

}
