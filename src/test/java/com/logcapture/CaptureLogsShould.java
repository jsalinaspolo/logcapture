package com.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.List;

import static com.logcapture.CaptureLogs.captureLogEvents;
import static com.logcapture.infrastructure.logback.StubAppender.STUB_APPENDER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;
import static org.slf4j.event.Level.INFO;

public class CaptureLogsShould {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Test
  public void capture_events() {
    List<ILoggingEvent> logEvents = captureLogEvents(() -> log.info("a message"));

    boolean anyMatch = logEvents.stream().anyMatch(e ->
      Level.valueOf(e.getLevel().levelStr) == INFO && e.getMessage().equals("a message"));

    assertThat(logEvents).hasSize(1);
    assertThat(anyMatch).isTrue();
  }

  @Test
  public void detach_appender_when_events_are_captured() {
    captureLogEvents(() -> log.info("a message"));
    assertStubAppenderIsDetached();
  }

  @Test
  public void detach_appender_when_capturing_events_an_exception_is_thrown() {
    assertThatThrownBy(() -> captureLogEvents(() -> {
      log.info("a message");
      throw new IllegalStateException();
    })).isInstanceOf(IllegalStateException.class);

    assertStubAppenderIsDetached();
  }

  private void assertStubAppenderIsDetached() {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
    assertThat(root.getAppender(STUB_APPENDER_NAME)).isNull();
  }
}
