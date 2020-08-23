Jersey2 - Guice Bridge with WebJar
==================================

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
  implementation "org.tuckey:urlrewritefilter:${urlRewriteVersion}"

  implementation "org.otasyn.template:angular-webjar:${webjarVersion}"

  providedCompile "javax.servlet:javax.servlet-api:{servletApiVersion}"
}
```

### Logging

In this project, logging is handled by SLF4J.  In order to work, it needs
dependencies for `slf4j-api` and a binding.  This project uses the binding
`slf4j-log4j12`.

In order to configure log4j logging, create the file
`log4j.properties` in `src/main/resources/`.

```bash
# Set the root log level.
log4j.rootLogger = ERROR, CONSOLE

# Define CONSOLE to be a ConsoleAppender.
log4j.appender.CONSOLE = org.apache.log4j.ConsoleAppender

# Configure CONSOLE to use PatternLayout.
log4j.appender.CONSOLE.layout = org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern = [%d{yyyy-MM-dd HH:mm:ss.SSS}] %p %c - %m%n
```

_See Also: http://www.slf4j.org/manual.html_

### WebJar

This project uses an WebJar containing an Angular client.  Angular
is not required for building a client WebJar.  In order to use the
the WebJar, add the publication as a dependency.

#### Contents

Beginning with Servlet 3.0, accessing the contents of the WebJar is
simple.  Other project types are compatible with WebJars, as described
in [the documentation](https://www.webjars.org/documentation).  This
project uses a WebJar that contains a file called `includes.jsp` that
contains generated `<script>` tags.  The `includes.jsp` file can be
included in a JSP to run the client code.

```jsp
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
  <head>
    <base href="${pageContext.request.contextPath}/" />
    <jsp:include page="/webjars/angular-webjar/0.1.0-SNAPSHOT/includes.jsp" />
  </head>
  <body>
    <app-root></app-root>
  </body>
</html>
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

### JSPs

In order to serve JSPs with Jersey, include the `jersey-mvc-jsp` dependency.

The `ApplicationPath` on the `ResourceConfig` should also be set to
something other than root.

```java
@ApplicationPath("/rest")
public class Config extends ResourceConfig {
  ...
}
```

#### URL Rewriting

In order to use Jersey to serve JSPs at the context root, URL rewriting
may be used.  This project uses UrlRewriteFilter.

Create a file called `urlrewrite.xml` in `WEB-INF`.

```xml
<urlrewrite>

  <rule>
    <name>Ignore-rest</name>
    <note>
      Do not change /rest requests.
    </note>
    <from>^/rest(/.*)?$</from>
    <to last="true">-</to>
  </rule>

  <rule>
    <name>Forward-Everything-Else</name>
    <note>
      Forward all other requests to /rest/views.
    </note>
    <from>^(/.*)?$</from>
    <to last="true">/rest/views</to>
  </rule>

</urlrewrite>
```

Original Source
---------------

This project has been adapted from a guide and source code by Zakaria Amine.

- **Guide** http://www.zakariaamine.com/2018-07-15/comment-on-jersey-guice-integration
- **Source** https://github.com/zak905/jersey2-guice-example
