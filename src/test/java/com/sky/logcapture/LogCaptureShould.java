package com.sky.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.event.Level.INFO;

public class LogCaptureShould {
  private final Logger log = LoggerFactory.getLogger(LogCaptureShould.class);

  @Test
  public void captureEvents() {
    List<ILoggingEvent> logEvents = LogCapture.captureLogEvents(() -> log.info("a message"));

    assertThat(logEvents.stream().anyMatch(e ->
      Level.valueOf(e.getLevel().levelStr) == INFO && e.getMessage().equals("a message")
    )).isTrue();
  }
}
