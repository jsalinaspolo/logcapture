package com.logcapture.junit;

import com.logcapture.assertion.VerificationException;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.qos.logback.classic.Level.DEBUG;
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class LogCaptureRuleShould {
  private static final String LOG_NAME = "aLogNotAttachedToRoot";
  private final Logger log = LoggerFactory.getLogger(LogCaptureRuleShould.class);

  @Rule
  public LogCaptureRule logCaptureRule = new LogCaptureRule();

  @Rule
  public LogCaptureRule logCaptureRuleAttached = new LogCaptureRule(LOG_NAME);

  @Test
  public void verify_sync_logs_using_rule() {
    log.info("a message");

    logCaptureRule.logged(aLog().info().withMessage("a message"));
  }

  @Test
  public void verify_log_when_is_not_in_root() {
    Logger logNotInRoot = createLogger(LOG_NAME);

    logNotInRoot.info("a message");

    assertThatExceptionOfType(VerificationException.class)
      .isThrownBy(() -> logCaptureRule.logged(aLog().info().withMessage("a message")));

    logCaptureRuleAttached.logged(aLog().info().withMessage("a message"));
  }

  private Logger createLogger(String name) {
    ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(name);
    logger.setLevel(DEBUG);
    logger.setAdditive(false);
    return logger;
  }
}
