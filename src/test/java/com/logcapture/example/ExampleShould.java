package com.logcapture.example;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import static org.hamcrest.Matchers.*;

public class ExampleShould {
  private final Logger log = LoggerFactory.getLogger(ExampleShould.class);

  @Test
  public void verify_captured_events() {
    captureLogEvents(() -> log.info("a message"))
      .logged(aMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")));
  }

  @Test
  public void verify_captured_events_having_assertions_as_result() {
    captureLogEvents(() -> {
      log.info("a message");
      return "aResult";
    })
      .assertions((result) -> assertThat(result).isEqualTo("aResult"))
      .logged(aMessage()
        .info()
        .withMessage("a message"));
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
  public void verify_captured_events_with_exception() {
    RuntimeException exception = new RuntimeException();

    captureLogEvents(() -> log.error("message", exception))
      .logged(aMessage()
        .havingException(logException()
          .withException(isA(RuntimeException.class))
        ));
  }

  @Test
  public void verify_captured_events_with_exception_cause_message() {
    RuntimeException exception = new RuntimeException(new IllegalStateException("Some state is invalid"));
    captureLogEvents(() -> log.error("message", exception))
      .logged(aMessage()
        .havingException(logException()
          .withException(whereCauseMessage(containsString("state is invalid")))
        ));
  }

  @Test
  public void verify_captured_events_with_exception_cause() {
    RuntimeException exception = new RuntimeException(new IllegalStateException("Some state is invalid"));
    captureLogEvents(() -> log.error("message", exception))
      .logged(aMessage()
        .havingException(logException()
          .withException(causeOf(IllegalStateException.class))
        ));
  }
}
