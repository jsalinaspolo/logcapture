package com.logcapture.assertion;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.INFO;
import static ch.qos.logback.classic.Level.WARN;
import static com.logcapture.assertion.ExpectedLoggedException.logException;
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog;
import static com.logcapture.matcher.exception.ExceptionCauseMatcher.causeOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class ExpectedLoggingMessageShould {

  @Test
  public void match_when_at_least_one_element_is_matching() {
    LoggingEvent matchingEvent = aLoggingEventWith(INFO, "message");
    LoggingEvent notMatchingEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().withLevel(equalTo(INFO));

    boolean matches = expectedLoggingMessage.matches(asList(matchingEvent, notMatchingEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void no_match_when_there_are_no_events() {
    ExpectedLoggingMessage expectedLoggingMessage = aLog().withLevel(equalTo(INFO));

    boolean matches = expectedLoggingMessage.matches(emptyList());

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_no_element_is_matching() {
    LoggingEvent notMatchingEvent1 = aLoggingEventWith(INFO, "message");
    LoggingEvent notMatchingEvent2 = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().withLevel(equalTo(DEBUG));

    boolean matches = expectedLoggingMessage.matches(asList(notMatchingEvent1, notMatchingEvent2));

    assertThat(matches).isFalse();
  }

  @Test
  public void match_when_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().withLevel(equalTo(INFO));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_debug_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(DEBUG, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().debug();

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_info_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().info();

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_warn_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(WARN, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().warn();

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_error_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(ERROR, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().error();

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_message_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog().withMessage(equalTo("message"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_multiple_messages_matches() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message has another message");

    ExpectedLoggingMessage expectedLoggingMessage = aLog().withMessage(containsString("message"),
        containsString("has"),
        containsString("another")
    );

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void not_match_when_a_message_not_matches() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message has another message");

    ExpectedLoggingMessage expectedLoggingMessage = aLog().withMessage(containsString("message"),
        containsString("has"),
        containsString("NO_MATCH"),
        containsString("another")
    );

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isFalse();
  }

  @Test
  public void match_when_length_message_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .length(equalTo(7));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_mdc_keys_match() {
    MDC.put("aKey", "someValue");
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMdc("aKey", equalTo("someValue"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_multiple_mdc_keys_match() {
    MDC.put("aKey", "someValue");
    MDC.put("anotherKey", "anotherValue");
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMdc("aKey", equalTo("someValue"))
        .withMdc("anotherKey", equalTo("anotherValue"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_logger_class_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withLoggerName(equalTo(ExpectedLoggingMessageShould.class.getName()));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_exception_class_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message", new RuntimeException());
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .havingException(logException()
            .withException(instanceOf(RuntimeException.class)));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_when_message_and_log_level_match() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("message"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void not_match_when_log_level_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMessage(equalTo("differentMessage"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_message_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMessage(equalTo("anotherMessage"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_message_length_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .length(equalTo(8));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_mdc_keys_different() {
    MDC.put("aKey", "differentValue");
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMdc("aKey", equalTo("someValue"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    MDC.clear();
    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_a_mdc_keys_is_different() {
    MDC.put("aKey", "differentValue");
    MDC.put("anotherKey", "anotherValue");
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMdc("aKey", equalTo("unmatchedValue"))
        .withMdc("anotherKey", equalTo("anotherValue"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    MDC.clear();
    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_logger_class_different() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withLoggerName(equalTo("anotherClassName"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isFalse();
  }

  @Test
  public void match_for_expected_marker() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    logEvent.setMarker(MarkerFactory.getMarker("A_MARKER"));

    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMarker(MarkerFactory.getMarker("A_MARKER"));

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void match_for_expected_marker_label() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    logEvent.setMarker(MarkerFactory.getMarker("A_MARKER"));

    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMarker("A_MARKER");

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isTrue();
  }

  @Test
  public void no_match_for_unexpected_marker() {
    LoggingEvent logEvent = aLoggingEventWith(INFO, "message");
    logEvent.setMarker(MarkerFactory.getMarker("A_MARKER"));

    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withMarker("ANOTHER_MARKER");

    boolean matches = expectedLoggingMessage.matches(singletonList(logEvent));

    assertThat(matches).isFalse();
  }

  @Test
  public void describe_failure_using_to_string() {
    ExpectedLoggingMessage expectedLoggingMessage = aLog()
        .withLevel(equalTo(ERROR))
        .withMessage(equalTo("message"))
        .length(equalTo(8))
        .withMdc("aKey", equalTo("some"))
        .withMarker("A_MARKER")
        .withLoggerName(equalTo("className"))
        .havingException(logException()
            .withMessage(equalTo("exception thrown"))
            .withException(causeOf(IllegalArgumentException.class)));

    assertThat(expectedLoggingMessage.toString())
        .contains("logLevelMatcher=<ERROR>")
        .contains("markerMatcher=<A_MARKER>")
        .contains("expectedMessageMatcher=[\"message\"]")
        .contains("expectedLengthMatcher=<8>")
        .contains("expectedMdc={aKey=\"some\"}")
        .contains("expectedLoggerNameMatcher=\"className\"")
        .contains("expectedLoggedException=ExpectedLoggedException{expectedMessageMatcher=\"exception thrown\", expectedException=Expecting exception to be instance of class java.lang.IllegalArgumentException");
  }

  private LoggingEvent aLoggingEventWith(Level level, String message) {
    return aLoggingEventWith(level, message, null);
  }

  private LoggingEvent aLoggingEventWith(Level level, String message, Exception exception) {
    Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    return new LoggingEvent("fqcn", log, level, message, exception, null);
  }
}
