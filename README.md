Jersey2 - Guice Bridge with WebJar
==================================

This is a skeleton app that demonstrate how certain frameworks can be used together.
This was meant as a starting point for standing up new applications.
In particular, ths project stands up a Java web application using several open source frameworks and the
[angular-webjar](https://github.com/otasyn/angular-webjar) project is a client app that is bundled up into
a WebJar to be deployed by this server app.

1) Jersey is a JAX-RS implementation used to create RESTful endpoints.
2) Guice is used for dependency injection (DI).  By default, Jersey uses HK2 for DI, so a major reason for
   this project is to demonstrate how to configure the
   [HK2 guice-bridge](https://github.com/javaee/hk2/tree/master/guice-bridge) to use Guice instead of HK2.
3) URLRewriteFilter is used to help create clean, human-readable URLs.
4) Shiro is used to add security to the application.  For this project, it primarily handles authentication
   and access control.
5) Typesafe Config is used to store environment-specific property values, such as app names and database
   connections that can be automatically applied at runtime for the appropriate environment.
6) It demonstrates how to deploy a client app stored in a WebJar, specifically [angular-webjar](https://github.com/otasyn/angular-webjar).
7) Spock is used to implement server-side unit testing.
8) Gradle is used to build and deploye the application.

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
  implementation "org.apache.shiro:shiro-core:${shiroVersion}"
  implementation "org.apache.shiro:shiro-web:${shiroVersion}"
  implementation "org.apache.shiro:shiro-jaxrs:${shiroVersion}"
  implementation "com.typesafe:config:${typesafeConfigVersion}"

  implementation "org.otasyn.template:angular-webjar:${webjarVersion}"

  providedCompile "javax.servlet:javax.servlet-api:{servletApiVersion}"

  testImplementation platform("org.spockframework:spock-bom:${spockVersion}")
  testImplementation "org.spockframework:spock-core"
  testImplementation "org.spockframework:spock-guice"
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
public class ProjectConfig extends ResourceConfig {
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

### Security

In order to secure the application, this project uses Shiro.  For the basic
collection of features for securing a web application, include the
`shiro-core` and `shiro-web` dependencies.  In order to add feature to allow
Shiro to work well with Jersey, include the `shiro-jaxrs` dependency.

#### Configuration

There are multiple ways to configure Shiro, but to keep it relatively simple,
this project uses `shiro.ini` in the `src/main/resources/` directory.

```bash
[main]
# If the user is unauthenticated, this tells Shiro where to redirect
# for the user to log in.
authc.loginUrl = /login

# Needed to cache the user authentication.
cacheManager = org.apache.shiro.cache.MemoryConstrainedCacheManager
securityManager.cacheManager = $cacheManager

[users]
# Create static users and password with roles and permissions.
# Do not use this method in production.
admin = secret,admin
reader = secret,samplereader
creator = secret,samplecreator
guest = secret

[roles]
# Define roles that may be given to users to determine access privileges.
admin = *
samplereader = sample:read
samplecreator = sample:read, sample:create

[urls]
# Determine the necessary security requirements for URL paths.
/ = anon
/webjars/** = anon

# The URL that will logout the user.
/logout = logout

# Everything will require authentication unless specified above
# or in annotations.
/** = authc
```

#### Annotations

The security requirements may also be specified using annotations,
such as `@RequiresAuthentication` and `@RequiresPermissions`.

```java
import org.apache.shiro.authz.annotation.RequiresPermissions;

public class SampleWebService {

  @GET
  @RequiresPermissions("sample:read")
  public String get() {
    LOG.debug("Get a sample message.");
    return sampleService.getMessage();
  }

}
```

### Typesafe Config

Typesafe Config enables a simple way to bring in config values from
a human-readable file in a JSON superset format.

#### Configuration File

Create `reference.conf` in `src/main/resources/`.  This file will contain
all the default values for the project.  Additional files, such as
`application.conf`, and system properties can override the configs in
`reference.conf`.

```javascript
org.otasyn.template.jersey2.guice {
  public {
    sample {
      message = "Hello from typesafe config."
    }
  }
}
```

#### Load Configuration

Since this project uses Guice, it loads the config and provides it
for injection into other classes.  It also uses a custom created
Guice binding annotation `TypesafeConfig`.  This helps Guice
identify provided instances for injection.

```java
public class ProjectGuiceModule extends AbstractModule {
  ...
  @Provides
  @TypesafeConfig
  Config provideTypesafeConfig() {
    return ConfigFactory.load().getConfig("org.otasyn.template.jersey2.guice");
  }
}
```

#### Inject Configuration

Now that the config has been provided by Guice, we can inject it into
other classes.

```java
public class DefaultSampleService implements SampleService {
  private Config sampleConfig;

  @Inject
  public DefaultSampleService {
    @TypesafeConfig Config config
  ) {
    this.sampleConfig = config.getConfig("public.sample");
  }
}
```

_See Also: https://lightbend.github.io/config/_

### Spock Testing

For unit test, this project uses the Spock framework.  To include this
as a test dependency, use the platform `spock-bom`, the include `spock-core`
and `spock-guice`.  To run the tests, use the `test` task.

```bash
$ ./gradlew test
```

#### Guice Module with Mocks

Within a Spock specification, normally you can create mocks.  However, in order
to bind mocks in a Guice module, use `DetachedMockFactory`.

```groovy
class MockGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    DetachedMockFactory dmf = new DetachedMockFactory();

    bind(AnotherService).toInstance(dmf.Mock(AnotherService))
    bind(SampleService).toInstance(dmf.Mock(SampleService))
  }

}
```

To use the Guice modules in a specification, use the `@UseModules` annotation.
To get an instance of the class being tested, inject a `Provider` with the class
as the generic type.  Then, call the `get()` method on that instance in `setup()`.

```groovy
@UseModule(MockGuiceModule)
class SampleWebServiceSpec extends Specification {
  
  @Inject Provider<SampleWebService> toTestProvider
  def toTest

  def setup() {
    toTest = toTestProvider.get()
  }

  ...
}
```

*See Also: http://spockframework.org/spock/docs/1.3/all_in_one.html*

Original Source
---------------

This project has been adapted from a guide and source code by Zakaria Amine.

- **Guide** http://www.zakariaamine.com/2018-07-15/comment-on-jersey-guice-integration
- **Source** https://github.com/zak905/jersey2-guice-example
