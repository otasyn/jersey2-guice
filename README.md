Jersey2 - Guice Bridge
======================

This is a template that combines Jersey 2 and Guice.  It uses the
[HK2 guice-bridge](https://github.com/javaee/hk2/tree/master/guice-bridge).

Adjustments
-----------

Since Java 11, JAXB has been removed and must be added as an separate
dependency.  So, the necessary dependencies have been added in order
to get the original source working.

```xml
<!-- API, java.xml.bind module -->
<dependency>
  <groupId>jakarta.xml.bind</groupId>
  <artifactId>jakarta.xml.bind-api</artifactId>
  <version>2.3.2</version>
</dependency>

<!-- Runtime, com.sun.xml.bind module -->
<dependency>
  <groupId>org.glassfish.jaxb</groupId>
  <artifactId>jaxb-runtime</artifactId>
  <version>2.3.2</version> 
</dependency>
```

Running
-------

To run the project:

```bash
$ mvn clean package jetty:run
```

To demonstrate that the servlet works:

```bash
$ curl http://localhost:8080/resource
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
packages("com.gwidgets.resource");
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
