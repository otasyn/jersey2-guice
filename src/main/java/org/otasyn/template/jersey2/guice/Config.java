package org.otasyn.template.jersey2.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.logging.Level;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.apache.shiro.web.jaxrs.ShiroFeature;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/rest")
public class Config extends ResourceConfig {

  private static final Logger LOG = LoggerFactory.getLogger(Config.class);

  private static final String PACKAGES = "org.otasyn.template.jersey2.guice";

  @Inject
  public Config(ServiceLocator serviceLocator) {
    LOG.debug(String.format("Set package for Jersey to scan: [%s].", PACKAGES));
    packages(PACKAGES);

    LOG.debug("Register JspMvcFeature.");
    property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/jsp");
    register(JspMvcFeature.class);

    LOG.debug("Register ShiroFeature.");
    register(ShiroFeature.class);

    LOG.debug("Initialize the Guice bridge.");
    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(Guice.createInjector(new ProjectGuiceModule()));
  }

}
