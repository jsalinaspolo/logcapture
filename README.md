[![Build Status](https://travis-ci.org/mustaine/logcapture.svg?branch=master)](https://travis-ci.org/mustaine/logcapture)
[![codecov](https://codecov.io/gh/mustaine/logcapture/branch/master/graph/badge.svg)](https://codecov.io/gh/mustaine/logcapture)

# LogCapture

LogCapture is a testing library for assert logging messages. 
 
## How it works

A simple example for assert a log message

```java
captureLogEvents(() -> log.info("a message"))
  .logged(aMessage()
    .withLevel(equalTo(INFO))
    .withMessage(equalTo("a message")))

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

* [The MIT License (MIT)] (http://opensource.org/licenses/MIT)

## Contributing

Github is for social coding: if you want to write code, I encourage contributions through pull requests from forks of this repository. 
Create Github tickets for bugs and new features and comment on the ones that you are interested in.
