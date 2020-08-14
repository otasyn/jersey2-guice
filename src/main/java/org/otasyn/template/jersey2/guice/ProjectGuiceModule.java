package org.otasyn.template.jersey2.guice;

import com.google.inject.AbstractModule;

import org.otasyn.template.jersey2.guice.sample.SampleGuiceModule;

public class ProjectGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new SampleGuiceModule());
  }

}
