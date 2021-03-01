package org.logcapture.assertion;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static ch.qos.logback.classic.Level.INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class ExpectedLoggedExceptionShould {

  @Test
  public void match_when_exception_class_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message", new IllegalArgumentException());
    ExpectedLoggedException expectedLoggedException = ExpectedLoggedException.logException()
        .withException(isA(IllegalArgumentException.class));

    boolean matches = expectedLoggedException.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_exception_message_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message", new IllegalArgumentException("message error"));
    ExpectedLoggedException expectedLoggedException = ExpectedLoggedException.logException()
        .withMessage(equalTo("message error"));

    boolean matches = expectedLoggedException.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void not_match_when_exception_class_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message", new IllegalArgumentException());
    ExpectedLoggedException expectedLoggedException = ExpectedLoggedException.logException()
        .withException(isA(IllegalStateException.class));

    boolean matches = expectedLoggedException.matches(logEvent);

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_exception_message_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message", new IllegalArgumentException("some error"));
    ExpectedLoggedException expectedLoggedException = ExpectedLoggedException.logException()
        .withMessage(equalTo("another error"));

    boolean matches = expectedLoggedException.matches(logEvent);

    assertThat(matches).isFalse();
  }

  @Test
  public void to_string_method_when_exception_not_match() {
    ExpectedLoggedException expectedLoggedException = ExpectedLoggedException.logException()
        .withMessage(equalTo("another error"))
        .withException(isA(IllegalArgumentException.class));

    assertThat(expectedLoggedException.toString()).isEqualTo("ExpectedLoggedException{expectedMessageMatcher=\"another error\", expectedException=is an instance of java.lang.IllegalArgumentException}");
  }

  private LoggingEvent aLoggingEventWith(Level level, String message, Exception exception) {
    Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    return new LoggingEvent("fqcn", log, level, message, exception, null);
  }
}
