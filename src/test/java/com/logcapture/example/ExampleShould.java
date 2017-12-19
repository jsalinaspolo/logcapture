package com.logcapture.example;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

import java.util.concurrent.CompletableFuture;

import static ch.qos.logback.classic.Level.INFO;
import static com.logcapture.LogCapture.captureLogEvents;
import static com.logcapture.LogCapture.captureLogEventsAsync;
import static com.logcapture.assertion.ExpectedLoggedException.logException;
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog;
import static com.logcapture.matcher.exception.ExceptionCauseMatcher.causeOf;
import static com.logcapture.matcher.exception.ExceptionCauseMessageMatcher.whereCauseMessage;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class ExampleShould {
  private final Logger log = LoggerFactory.getLogger(ExampleShould.class);

  private ServiceThatLogs underTest = new ServiceThatLogs();

  @Test
  public void verify_captured_events() {
    captureLogEvents(() -> underTest.methodThatLogsStuff())
      .logged(aLog().info()
        .withMessage("a message"));
  }

  @Test
  public void verify_captured_events_with_marker() {
    captureLogEvents(() -> log.info(MarkerFactory.getMarker("a_marker"), "a message"))
      .logged(aLog()
        .withLevel(equalTo(INFO))
        .withMarker("a_marker")
        .withMessage(equalTo("a message")));
  }

  @Test
  public void verify_captured_events_having_assertions_as_result() {
    captureLogEvents(() -> underTest.methodThatLogsStuffAndReturns("aResult"))
      .assertions((result) -> assertThat(result).isEqualTo("aResult"))
      .logged(aLog()
        .info()
        .withMessage("a message"));
  }

  @Test
  public void verify_captured_events_async_with_assertions() {
    captureLogEventsAsync(() -> CompletableFuture.supplyAsync(() -> underTest.methodThatLogsStuffAndReturns("aResult")))
      .waitAtMost(ofSeconds(1), aLog()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")))
      .assertions(result -> assertThat(result).isCompletedWithValue("aResult"));
  }

  @Test
  public void verify_captured_events_with_exception() {
    RuntimeException exception = new RuntimeException();

    captureLogEvents(() -> underTest.methodThatLogs(exception))
      .logged(aLog()
        .havingException(logException()
          .withException(isA(RuntimeException.class))
        ));
  }

  @Test
  public void verify_captured_events_with_exception_cause_message() {
    RuntimeException exception = new RuntimeException(new IllegalStateException("Some state is invalid"));
    captureLogEvents(() -> underTest.methodThatLogs(exception))
      .logged(aLog()
        .havingException(logException()
          .withException(whereCauseMessage(containsString("state is invalid")))
        ));
  }

  @Test
  public void verify_captured_events_with_exception_cause() {
    RuntimeException exception = new RuntimeException(new IllegalStateException("Some state is invalid"));
    captureLogEvents(() -> underTest.methodThatLogs(exception))
      .logged(aLog()
        .havingException(logException()
          .withException(causeOf(IllegalStateException.class))
        ));
  }

  @Test
  public void verify_mdc_keys() {
    MDC.put("aKey", "someValue");

    captureLogEvents(() -> underTest.methodThatLogsStuff())
      .logged(aLog().info()
        .withMdc("aKey", equalTo("someValue"))
        .withMessage("a message"));
  }

  class ServiceThatLogs {

    void methodThatLogsStuff() {
      log.info("a message");
      log.info("another message");
    }

    String methodThatLogsStuffAndReturns(String result) {
      log.info("a message");
      return result;
    }

    void methodThatLogs(Exception exception) {
      log.error("message", exception);
    }
  }
}
