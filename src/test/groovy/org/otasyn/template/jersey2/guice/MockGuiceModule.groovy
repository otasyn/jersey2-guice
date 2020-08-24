package org.otasyn.template.jersey2.guice

import com.google.inject.AbstractModule

import spock.mock.DetachedMockFactory

import org.otasyn.template.jersey2.guice.sample.AnotherService
import org.otasyn.template.jersey2.guice.sample.SampleService

class MockGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    DetachedMockFactory dmf = new DetachedMockFactory();

    bind(AnotherService).toInstance(dmf.Mock(AnotherService))
    bind(SampleService).toInstance(dmf.Mock(SampleService))
  }

}
