package org.logcapture.junit5;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.logcapture.assertion.ExpectedLoggingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.INFO;
import static org.hamcrest.Matchers.equalTo;
import static org.logcapture.assertion.ExpectedLoggingMessage.aLog;

class LogCaptureRegisterExtensionShould {
  private final Logger log = LoggerFactory.getLogger(LogCaptureRegisterExtensionShould.class);

  @RegisterExtension
  LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

  @Test
  void verify_missing_events() {
    logCaptureExtension.logged(Matchers.not(ExpectedLoggingMessage.aLog()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("missing message"))));
  }

  @Test
  void verify_multiple_events() {
    log.info("first message");
    log.debug("second message");
    logCaptureExtension.logged(
        Matchers.allOf(
            ExpectedLoggingMessage.aLog().withLevel(equalTo(INFO)).withMessage(equalTo("first message")),
            ExpectedLoggingMessage.aLog().withLevel(equalTo(DEBUG)).withMessage(equalTo("second message"))
        )
    );
  }

  @Test
  void verify_sync_logs_using_rule() {
    log.info("a message");

    logCaptureExtension.logged(ExpectedLoggingMessage.aLog().info().withMessage("a message"));
  }

  @Test
  void verify_log_n_times() {
    log.info("a message");
    log.info("a message");

    ExpectedLoggingMessage expectedMessage = aLog()
            .info()
            .withMessage("a message");
    logCaptureExtension
            .filter(expectedMessage)
            .logged(expectedMessage, 2);
  }
}
