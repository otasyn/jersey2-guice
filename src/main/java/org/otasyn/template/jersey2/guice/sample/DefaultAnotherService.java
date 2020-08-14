package org.otasyn.template.jersey2.guice.sample;

import org.otasyn.template.jersey2.guice.sample.AnotherService;

public class DefaultAnotherService implements AnotherService {

  @Override
  public String provideName() {
    return "from another service";
  }

}
