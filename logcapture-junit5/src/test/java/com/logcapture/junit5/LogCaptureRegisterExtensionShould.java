package com.logcapture.junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.INFO;
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

class LogCaptureRegisterExtensionShould {
  private final Logger log = LoggerFactory.getLogger(LogCaptureRegisterExtensionShould.class);

  @RegisterExtension
  LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

  @Test
  void verify_missing_events() {
    logCaptureExtension.logged(not(aLog()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("missing message"))));
  }

  @Test
  void verify_multiple_events() {
    log.info("first message");
    log.debug("second message");
    logCaptureExtension.logged(allOf(aLog().withLevel(equalTo(INFO)).withMessage(equalTo("first message")),
        aLog().withLevel(equalTo(DEBUG)).withMessage(equalTo("second message"))));
  }

  @Test
  void verify_sync_logs_using_rule() {
    log.info("a message");

    logCaptureExtension.logged(aLog().info().withMessage("a message"));
  }
}
