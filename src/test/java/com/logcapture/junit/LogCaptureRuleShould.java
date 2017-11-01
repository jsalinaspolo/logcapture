package com.logcapture.junit;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.logcapture.assertion.ExpectedLoggingMessage.aLog;

public class LogCaptureRuleShould {
  private final Logger log = LoggerFactory.getLogger(LogCaptureRuleShould.class);

  @Rule
  public LogCaptureRule logCaptureRule = new LogCaptureRule();

  @Test
  public void verify_sync_logs_using_rule() {
    log.info("a message");

    logCaptureRule.logged(aLog().info().withMessage("a message"));
  }
}
