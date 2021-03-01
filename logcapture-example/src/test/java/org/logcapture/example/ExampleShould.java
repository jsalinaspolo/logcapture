package org.logcapture.example;

import org.logcapture.junit4.LogCaptureRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

import static ch.qos.logback.classic.Level.INFO;
import static org.logcapture.assertion.ExpectedLoggedException.logException;
import static org.logcapture.assertion.ExpectedLoggingMessage.aLog;
import static org.logcapture.matcher.exception.ExceptionCauseMatcher.causeOf;
import static org.logcapture.matcher.exception.ExceptionCauseMessageMatcher.whereCauseMessage;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class ExampleShould {
  private final Logger log = LoggerFactory.getLogger(ExampleShould.class);

  @Rule
  public LogCaptureRule logCaptureRule = new LogCaptureRule();

  private ServiceThatLogs underTest = new ServiceThatLogs();

  @Test
  public void verify_captured_events() {
    underTest.methodThatLogsStuff();
    logCaptureRule.logged(aLog().info()
        .withMessage("a message"));
  }

  @Test
  public void verify_captured_events_with_marker() {
    log.info(MarkerFactory.getMarker("a_marker"), "a message");
    logCaptureRule.logged(aLog()
        .withLevel(equalTo(INFO))
        .withMarker("a_marker")
        .withMessage(equalTo("a message")));
  }

  @Test
  public void verify_captured_events_with_exception() {
    RuntimeException exception = new RuntimeException();

    underTest.methodThatLogs(exception);
    logCaptureRule.logged(aLog()
        .havingException(logException()
            .withException(isA(RuntimeException.class))
        ));
  }

  @Test
  public void verify_captured_events_with_exception_cause_message() {
    RuntimeException exception = new RuntimeException(new IllegalStateException("Some state is invalid"));
    underTest.methodThatLogs(exception);
    logCaptureRule.logged(aLog()
        .havingException(logException()
            .withException(whereCauseMessage(containsString("state is invalid")))
        ));
  }

  @Test
  public void verify_captured_events_with_exception_cause() {
    RuntimeException exception = new RuntimeException(new IllegalStateException("Some state is invalid"));
    underTest.methodThatLogs(exception);
    logCaptureRule.logged(aLog()
        .havingException(logException()
            .withException(causeOf(IllegalStateException.class))
        ));
  }

  @Test
  public void verify_mdc_keys() {
    MDC.put("aKey", "someValue");

    underTest.methodThatLogsStuff();
    logCaptureRule.logged(aLog().info()
        .withMdc("aKey", equalTo("someValue"))
        .withMessage("a message"));
  }

  class ServiceThatLogs {

    void methodThatLogsStuff() {
      log.info("a message");
      log.info("another message");
    }

    void methodThatLogs(Exception exception) {
      log.error("message", exception);
    }
  }
}
