package org.logcapture.junit4;

import org.junit.Rule;
import org.junit.Test;
import org.logcapture.assertion.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.INFO;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.*;
import static org.logcapture.assertion.ExpectedLoggingMessage.aLog;

public class LogCaptureRuleShould {
  private static final String LOG_NAME = "aLogNotAttachedToRoot";
  private final Logger log = LoggerFactory.getLogger(LogCaptureRuleShould.class);

  @Rule
  public LogCaptureRule logCaptureRule = new LogCaptureRule();

  @Rule
  public LogCaptureRule logCaptureRuleAttached = new LogCaptureRule(LOG_NAME);

  @Test
  public void verify_missing_events() {
    logCaptureRule.logged(not(aLog()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("missing message"))));
  }

  @Test
  public void verify_multiple_events() {
    log.info("first message");
    log.debug("second message");
    logCaptureRule.logged(allOf(
        aLog().withLevel(equalTo(INFO)).withMessage(equalTo("first message")),
        aLog().withLevel(equalTo(DEBUG)).withMessage(equalTo("second message"))
    ));
  }

  @Test
  public void verify_sync_logs_using_rule() {
    log.info("a message");

    logCaptureRule.logged(aLog().info().withMessage("a message"));
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
  public void verify_log_when_is_not_in_root() {
    Logger logNotInRoot = createLogger(LOG_NAME);

    logNotInRoot.info("a message");

    assertThatExceptionOfType(VerificationException.class)
        .isThrownBy(() -> logCaptureRule.logged(aLog().info().withMessage("a message")));

    logCaptureRuleAttached.logged(aLog().info().withMessage("a message"));
  }

  @Test
  public void verify_log_with_mdc_keys() {
    MDC.put("a-key", "a-value");
    log.info("a message");

    logCaptureRule.logged(aLog()
        .info()
        .withMdc("a-key", "a-value")
        .withMessage("a message"));
  }

  @Test
  public void verify_log_n_times() {
    log.info("a message");
    log.info("a message");

    logCaptureRule.logged(aLog().info().withMessage("a message"), 2);
  }

  private Logger createLogger(String name) {
    ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(name);
    logger.setLevel(DEBUG);
    logger.setAdditive(false);
    return logger;
  }
}
