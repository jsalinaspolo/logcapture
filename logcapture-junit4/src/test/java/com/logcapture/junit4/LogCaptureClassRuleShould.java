package com.logcapture.junit4;

import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.logcapture.assertion.ExpectedLoggingMessage.aLog;

public class LogCaptureClassRuleShould {
  private final Logger log = LoggerFactory.getLogger(LogCaptureClassRuleShould.class);

  @ClassRule
  public static LogCaptureRule logCaptureRule = new LogCaptureRule();

  @Test
  public void verify_sync_logs_using_rule() {
    log.info("a message");

    logCaptureRule.logged(aLog().info().withMessage("a message"));
  }
}
