package org.otasyn.template.jersey2.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.otasyn.template.jersey2.guice.sample.SampleGuiceModule;

public class ProjectGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new SampleGuiceModule());
  }

  @Provides
  @TypesafeConfig
  Config provideTypesafeConfig() {
    return ConfigFactory.load().getConfig("org.otasyn.template.jersey2.guice");
  }

}
