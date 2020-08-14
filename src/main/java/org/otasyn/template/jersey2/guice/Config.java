package org.otasyn.template.jersey2.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

@ApplicationPath("/")
public class Config extends ResourceConfig {

  @Inject
  public Config(ServiceLocator serviceLocator) {
    packages("org.otasyn.template.jersey2.guice");

    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(Guice.createInjector(new ProjectGuiceModule()));
  }

}
