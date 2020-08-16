Jersey2 - Guice Bridge
======================

This is a template that combines Jersey 2 and Guice.  It uses the
[HK2 guice-bridge](https://github.com/javaee/hk2/tree/master/guice-bridge).

Adjustments
-----------

Since Java 11, JAXB has been removed and must be added as an separate
dependency.  So, the necessary dependencies have been added.

```groovy
dependencies {
  ...
  implementation 'jakarta.xml.bind:jakarta.xml.bind-api:2.3.2'
  implementation 'org.glassfish.jaxb:jaxb-runtime:2.3.2'
  ...
}
```

Gradle
------

One purpose of this project is to build using Gradle, so the Maven `pom.xml`
has been adapted to a Gradle `build.gradle` and other related files.  Mostly,
this is just reconfiguring dependencies.

### Dependencies

Dependencies are listed within a `dependencies` block.

```groovy
dependencies {
  implementation "org.glassfish.jersey.containers:jersey-container-servlet:${jerseyVersion}"
  implementation "org.glassfish.jersey.ext:jersey-mvc-jsp:${jerseyVersion}"
  implementation "org.glassfish.jersey.inject:jersey-hk2:${jerseyVersion}"
  implementation "org.glassfish.hk2:guice-bridge:${guiceBridgeVersion}"
  implementation "com.google.inject:guice:${guiceVersion}"
  implementation "jakarta.xml.bind:jakarta.xml.bind-api:${jaxbVersion}"
  implementation "org.glassfish.jaxb:jaxb-runtime:${jaxbVersion}"
  providedCompile "javax.servlet:javax.servlet-api:{servletApiVersion}"
}
```

### JSPs

In order to serve JSPs with Jersey, include the `jersey-mvc-jsp` dependency.

```groovy
dependencies {
  ...
  implementation "org.glassfish.jersey.ext:jersey-mvc-jsp:${jerseyVersion}"
  ...
}
```

The `ApplicationPath` on the `ResourceConfig` should also be set to
something other than root.

```java
@ApplicationPath("/rest")
public class Config extends ResourceConfig {
  ...
}
```

### Servlet WAR

To generate a WAR, include the `war` plugin.  Some configurations can be made,
but it works pretty well using the default configuration.

### Building

Build the project using the Gradle task `build`.

```bash
$ ./gradlew build
```

### Running

Instead of directly generating and deploying a WAR, the project can be run
using Gretty.  There are various configurations available, but this project
only sets the `contextPath` and `servletContainer`.  By default, the
`servletContainer` is set to `jetty9.4`, but other versions of Jetty or
Tomcat can be used.

```groovy
gretty {
  contextPath = 'jersey2-guice'
  servletContainer = 'jetty9.4'
}
```

Launch the servlet container using `appRun`.

```bash
$ ./gradlew appRun
```

Use curl or a browser to demonstrate that the servlet works.

```bash
$ curl http://localhost:8080/jersey2-guice/sample
```

Explanation
-----------

### ResourceConfig

`ResourceConfig` is Jersey's implementation of JAX-RS's `Application`.
It is used to configure the servlet.

#### packages

In the constructor, use the `packages` method to tell Jersey where to
scan for resources.  It will scan packages recursively, so it is not
necessary to include every package if they can all be safely found
within a single parent package.

```java
packages("org.otasyn.template.jersey2.guice");
```

#### GuiceIntoHK2Bridge

Jersey uses HK2, so in order to get Guice to work alongside HK2,
`GuiceIntoHK2Bridge` is used.

```java
GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
guiceBridge.bridgeGuiceInjector(Guice.createInjector(new GuiceModule()));
```

### AbstractModule

`AbstractModule` is Guice's abstract class that is used to configure
dependency injection.

_See Also: https://github.com/google/guice/wiki_

### Path, GET, POST, etc

- `Path` is the JAX-RS annotation that tells Jersey the entry points
for HTTP requests.
- `GET`, `POST`, and other similar annotations describes the request
method that will be handled by the method.

Original Source
---------------

This project has been adapted from a guide and source code by Zakaria Amine.

- **Guide** http://www.zakariaamine.com/2018-07-15/comment-on-jersey-guice-integration
- **Source** https://github.com/zak905/jersey2-guice-example
