package com.logcapture;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.qos.logback.classic.Level.INFO;
import static com.logcapture.LogCapture.captureLogEvents;
import static com.logcapture.assertion.ExpectedLoggingMessage.logMessage;
import static org.hamcrest.Matchers.equalTo;

public class LogCaptureShould {
  private final Logger log = LoggerFactory.getLogger(LogCaptureShould.class);

  @Test
  public void verify_captured_events() {
    captureLogEvents(() -> log.info("a message"))
      .containMessage(logMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a message")));
  }

  @Test
  public void fail_when_verify_captured_events_not_found() {
    Assertions.assertThatThrownBy(() ->     captureLogEvents(() -> log.info("a message"))
      .containMessage(logMessage()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("a different message"))))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("No Log Found for [ExpectedLoggingMessage{logLevelMatcher=<INFO>, expectedMessageMatcher=\"a different message\", expectedMdc={}}]");
  }
}
