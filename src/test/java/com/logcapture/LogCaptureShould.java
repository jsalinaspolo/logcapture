package com.logcapture;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;

import static ch.qos.logback.classic.Level.INFO;
import static com.logcapture.LogCapture.captureLogEvents;
import static com.logcapture.LogCapture.captureLogEventsAsync;
import static com.logcapture.assertion.ExpectedLoggedException.logException;
import static com.logcapture.assertion.ExpectedLoggingMessage.aMessage;
import static com.logcapture.matcher.exception.ExceptionCauseMatcher.causeOf;
import static com.logcapture.matcher.exception.ExceptionCauseMessageMatcher.whereCauseMessage;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.equalTo;

public class LogCaptureShould {
  private final Logger log = LoggerFactory.getLogger(LogCaptureShould.class);

  @Test
  public void verify_captured_events() {
    captureLogEvents(() -> log.info("a message"))
      .logged(aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")));
  }

  @Test
  public void verify_captured_events_without_result_assertion_null() {
    captureLogEvents(() -> log.info("a message"))
      .logged(aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")))
      .assertions(result -> assertThat(result).isNull());
  }

  @Test
  public void verify_captured_events_having_assertions_as_result() {
    captureLogEvents(() -> {
      log.info("a message");
      return "aResult";
    })
      .assertions((result) -> assertThat(result).isEqualTo("aResult"))
      .logged(aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")));
  }

  @Test
  public void verify_captured_events_without_assertions() {
    captureLogEvents(() -> {
      log.info("a message");
      return "aResult";
    })
      .logged(aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")))
      .assertions((result) -> assertThat(result).isEqualTo("aResult"));
  }

  @Test
  public void verify_captured_async_logs_of_runnable() {
    captureLogEventsAsync(() -> new Thread(() -> log.info("a message")).start())
      .waitAtMost(ofSeconds(1), aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")))
      .assertions(result -> assertThat(result).isNull());
  }

  @Test
  public void verify_captured_events_async_with_assertions() {
    captureLogEventsAsync(() -> CompletableFuture.supplyAsync(() -> {
      log.info("a message");
      return "aResult";
    }))
      .waitAtMost(ofSeconds(1), aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")))
      .assertions(result -> assertThat(result).isCompletedWithValue("aResult"));
  }

  @Test
  public void fail_when_verify_captured_events_not_found() {
    assertThatThrownBy(() -> captureLogEvents(() -> log.info("a message"))
      .logged(aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a different message"))))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("No Log Found for [ExpectedLoggingMessage{logLevelMatcher=<INFO>, expectedMessageMatcher=\"a different message\", expectedMdc={}}]");
  }

  @Test
  public void throws_exception_when_fail_to_verify_captured_events_with_exception_message_not_match() {
    AssertionsForClassTypes.assertThatThrownBy(() ->
      LogbackInterceptor.captureLogEvents(() -> log.info("message", new RuntimeException(
        new IllegalStateException("Some state is invalid"))))
        .logged(aMessage()
          .havingException(logException()
            .withException(whereCauseMessage(equalTo("another cause message")))
          ))
    ).hasMessageContaining("Expecting exception cause to contain \"another cause message\"");
  }

  @Test
  public void throw_exception_when_fail_to_verify_captured_events_with_exception_cause_not_match() {
    assertThatThrownBy(() ->
      LogbackInterceptor.captureLogEvents(() -> log.info("message", new RuntimeException(new SocketTimeoutException())))
        .logged(aMessage()
          .havingException(logException()
            .withException(causeOf(IllegalStateException.class))))
    ).hasMessageContaining("Expecting exception to be instance of class java.lang.IllegalStateException");
  }
}
