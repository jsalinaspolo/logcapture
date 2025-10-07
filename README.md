[![Build Status](https://travis-ci.org/jsalinaspolo/logcapture.svg?branch=master)](https://travis-ci.org/jsalinaspolo/logcapture)
[![Sonatype Nexus](https://img.shields.io/nexus/r/org.logcapture/logcapture-core?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://repo1.maven.org/maven2/org/logcapture/)
[![codecov](https://codecov.io/gh/jsalinaspolo/logcapture/branch/master/graph/badge.svg)](https://codecov.io/gh/jsalinaspolo/logcapture)
[![Known Vulnerabilities](https://snyk.io/test/github/jsalinaspolo/logcapture/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/jsalinaspolo/logcapture?targetFile=build.gradle)

# LogCapture

LogCapture is a testing library for asserting logging messages.

> :warning: Latest release with Java 1.8 and/or spock 1.0 support is `1.3.4` 
## How it works


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

Using JUnit 5 Extension:

```java
@RegisterExtension
public LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

@Test
public void verify_logs_using_extension() {
  log.info("a message");

  logCaptureExtension.logged(aLog().info().withMessage("a message"));
}
```

Using Spock:

```groovy
class LogCaptureSpecShould extends LogCaptureSpec {

  @Shared log = LoggerFactory.getLogger(getClass())

  def "verify log message"() {
    expect:
    log.info("a message");

    logged(aLog().info().withMessage("a message"))
  }
}
```

Using Kotest:

```kotlin
class LogCaptureListenerSpec : StringSpec({
  val logCaptureListener = LogCaptureListener()
  listener(logCaptureListener)  // Add LogCaptureListener

  val log: Logger = LoggerFactory.getLogger(LogCaptureListenerSpec::class.java)

  "verify log messages" {
    log.info("a message")

    logCaptureListener.logged(aLog().info().withMessage("a message"))
  }
})
```

Using Kotest v6:

```kotlin
class LogCaptureListenerSpec : StringSpec({
  val logCaptureListener = LogCaptureListener()
  extensions(logCaptureListener)  // Add LogCaptureListener

  val log: Logger = LoggerFactory.getLogger(LogCaptureListenerSpec::class.java)

  "verify log messages" {
    log.info("a message")

    logCaptureListener.logged(aLog().info().withMessage("a message"))
  }
})
```

More example how to use the library at [ExampleShould.java](https://github.com/jsalinaspolo/logcapture/blob/main/logcapture-example/src/test/java/org/logcapture/example/ExampleShould.java)

## Binaries

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at [http://search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.logcapture).

Gradle

```
testImplementation 'org.logcapture:logcapture-core:x.y.z'
```

add one of the test library dependency 

```
testImplementation 'org.logcapture:logcapture-junit4:x.y.z'
testImplementation 'org.logcapture:logcapture-junit5:x.y.z'
testImplementation 'org.logcapture:logcapture-spock2:x.y.z'
testImplementation 'org.logcapture:logcapture-kotest:x.y.z'
testImplementation 'org.logcapture:logcapture-kotest6:x.y.z'
```


Maven:

```xml
<dependency>
    <groupId>org.logcapture</groupId>
    <artifactId>logcapture-core</artifactId>
    <version>x.y.z</version>
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
