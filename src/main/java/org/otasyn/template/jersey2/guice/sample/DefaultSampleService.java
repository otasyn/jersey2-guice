package org.otasyn.template.jersey2.guice.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.otasyn.template.jersey2.guice.sample.AnotherService;
import org.otasyn.template.jersey2.guice.sample.SampleService;

@Singleton
public class DefaultSampleService implements SampleService {

  @Inject AnotherService anotherService;

  @Override
  public String getMessage() {
    return "Hi " + anotherService.provideName();
  }

}
