[![Build Status](https://travis-ci.org/mustaine/logcapture.svg?branch=master)](https://travis-ci.org/mustaine/logcapture)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jspcore/logcapture/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jspcore/logcapture)
[![codecov](https://codecov.io/gh/mustaine/logcapture/branch/master/graph/badge.svg)](https://codecov.io/gh/mustaine/logcapture)

# LogCapture

LogCapture is a testing library for assert logging messages. 
 
## How it works

Using the DSL:

```java
captureLogEvents(() -> log.info("a message"))
  .logged(aLog().info()
    .withMessage("a message"))

```

Using JUnit Rule:

```java
@Rule
public LogCaptureRule logCaptureRule = new LogCaptureRule();

@Test
public void verify_logs_using_rule() {
  log.info("a message");

  logCaptureRule.logged(aLog().info().withMessage("a message"));
}
```

More example how to use the library at [ExampleShould.java](https://github.com/mustaine/logcapture/blob/master/src/test/java/com/logcapture/example/ExampleShould.java) 


## Binaries

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at [http://search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.jspcore%22%20AND%20a%3A%22logcapture%22).

Example for Maven:

```xml
<dependency>
    <groupId>com.jspcore</groupId>
    <artifactId>logcapture</artifactId>
    <version>x.y</version>
</dependency>
```

## Why LogCapture?

Logging should be a **first class citizen** in every system that aims to be easily diagnosed and maintained. Logging/testing first could help 
you to drive production code. At the same time it is easy to log object references and objects that includes private information like passwords or tokens 
and not realising until we actually read production logs.

We should test how robust are our non-functional capabilities, and not only our functional features. Being able to diagnose, 
and ultimately fix, issues is a non-functional dimension that should be subject to the same standards as performance, reliability or security.

Logging first development could give you the following benefits:

* Help you to come up with some useful logging that makes sense in context, that exposes enough, and just enough, semantic 
information and that does not leak secure information.
* Help you to understand beforehand what are the high level technical details that your design will implement.
* Provide insights to security, support or operations engineers that could have different needs and drivers that application developers.
* Help you to come up with rules for your logging monitoring system.

## License

This project is licensed under [MIT license](http://opensource.org/licenses/MIT).

## Contributing

Github is for social coding: if you want to write code, I encourage contributions through pull requests from forks of this repository. 
Create Github tickets for bugs and new features and comment on the ones that you are interested in.
