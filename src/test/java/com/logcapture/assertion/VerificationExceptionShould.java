package com.logcapture.assertion;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.logcapture.assertion.ExpectedLoggingMessage.aLog;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

public class VerificationExceptionShould {

  @Test
  public void containLogsEventsNotMatchingExpected() {
    ExpectedLoggingMessage expectedLogMessage = aLog().info().withMessage("a log message");
    List<ILoggingEvent> logEvents = Arrays.asList(
      aLoggingEventWith(Level.INFO, "a different message"),
      aLoggingEventWith(Level.INFO, "another different message")
    );

    VerificationException verificationException = VerificationException.forUnmatchedLog(expectedLogMessage, logEvents);

    assertThat(verificationException.toString()).isEqualTo("com.logcapture.assertion.VerificationException: Expected matching: \n" +
      "ExpectedLoggingMessage{logLevelMatcher=<INFO>, expectedMessageMatcher=[\"a log message\"], expectedMdc={}}\n" +
      "Logs received: \n" +
      "level: INFO marker: null mdc: {} message: a different message\n" +
      "level: INFO marker: null mdc: {} message: another different message");
  }

  @Test
  public void containLogsEventsNotMatchingExpectedWithMdcKeys() {
    Map<String, String> mdcKeys = new HashMap<String, String>() {{
      put("aKey", "aValue");
      put("anotherKey", "anotherValue");
    }};

    ExpectedLoggingMessage expectedLogMessage = aLog().info()
      .withMessage("a log message")
      .withMdc("aKey", equalTo("aValue"))
      .withMdc("anotherKey", equalTo("anotherValue"));

    List<ILoggingEvent> logEvents = Arrays.asList(
      aLoggingEventWith(Level.INFO, "a different message", mdcKeys),
      aLoggingEventWith(Level.INFO, "another different message", mdcKeys)
    );

    VerificationException verificationException = VerificationException.forUnmatchedLog(expectedLogMessage, logEvents);

    assertThat(verificationException.toString()).isEqualTo("com.logcapture.assertion.VerificationException: Expected matching: \n" +
      "ExpectedLoggingMessage{logLevelMatcher=<INFO>, expectedMessageMatcher=[\"a log message\"], expectedMdc={anotherKey=\"anotherValue\", aKey=\"aValue\"}}\n" +
      "Logs received: \n" +
      "level: INFO marker: null mdc: {anotherKey=anotherValue, aKey=aValue} message: a different message\n" +
      "level: INFO marker: null mdc: {anotherKey=anotherValue, aKey=aValue} message: another different message");
  }

  private LoggingEvent aLoggingEventWith(Level level, String message) {
    return aLoggingEventWith(level, message, emptyMap());
  }

  private LoggingEvent aLoggingEventWith(Level level, String message, Map<String, String> mdcKeys) {
    Logger log = (Logger) LoggerFactory.getLogger(getClass());
    LoggingEvent logEvent = new LoggingEvent("fqcn", log, level, message, null, null);
    logEvent.setMDCPropertyMap(mdcKeys);
    return logEvent;
  }
}
