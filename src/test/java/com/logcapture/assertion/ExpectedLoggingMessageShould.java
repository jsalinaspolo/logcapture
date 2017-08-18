package com.logcapture.assertion;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class ExpectedLoggingMessageShould {

  @Test
  public void match_when_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage().withLevel(equalTo(INFO));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_message_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage().withMessage(equalTo("message"));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_length_message_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .length(equalTo(7));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_mdc_keys_match() {
    MDC.put("aKey", "someValue");
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withMdc("aKey", equalTo("someValue"));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_logger_class_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withLoggerName(equalTo(ExpectedLoggingMessageShould.class.getName()));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_exception_class_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message", new RuntimeException());
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .havingException(ExpectedLoggedException.logException()
        .withException(instanceOf(RuntimeException.class)));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_message_and_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withLevel(equalTo(INFO))
      .withMessage(equalTo("message"));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isTrue();
  }

  @Test
  public void not_match_when_log_level_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withMessage(equalTo("differentMessage"));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_message_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withMessage(equalTo("anotherMessage"));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_message_length_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .length(equalTo(8));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_mdc_keys_different() {
    MDC.put("aKey", "differentValue");
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withMdc("aKey", equalTo("someValue"));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_logger_class_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withLoggerName(equalTo("anotherClassName"));

    boolean matches = expectedLoggingMessage.matches(logEvent);

    assertThat(matches).isFalse();
  }

  @Test
  public void describe_failure_using_to_string() {
    ExpectedLoggingMessage expectedLoggingMessage = ExpectedLoggingMessage.logMessage()
      .withLevel(equalTo(ERROR))
      .withMessage(equalTo("message"))
      .length(equalTo(8))
      .withMdc("aKey", equalTo("some"))
      .withLoggerName(equalTo("className"));

    assertThat(expectedLoggingMessage.toString())
      .contains("logLevelMatcher=<ERROR>")
      .contains("expectedMessageMatcher=\"message\"")
      .contains("expectedLengthMatcher=<8>")
      .contains("expectedMdc={aKey=\"some\"}")
      .contains("expectedLoggerNameMatcher=\"className\"");
  }

  private LoggingEvent aLoggingEventWith(Level level, String message) {
    return aLoggingEventWith(level, message, null);
  }

  private LoggingEvent aLoggingEventWith(Level level, String message, Exception exception) {
    Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    return new LoggingEvent("fqcn", log, level, message, exception, null);
  }
}
