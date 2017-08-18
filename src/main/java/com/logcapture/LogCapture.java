package com.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.logcapture.assertion.ExpectedLoggingMessage;

import java.util.List;

public class LogCapture {

  private final List<ILoggingEvent> events;

  private LogCapture(List<ILoggingEvent> events) {
    this.events = events;
  }

  public LogCapture containMessage(ExpectedLoggingMessage expectedLoggingMessage) {
    for (ILoggingEvent event : events) {
      if (expectedLoggingMessage.matches(event)) {
        return this;
      }
    }

    throw new RuntimeException("No Log Found for [" + expectedLoggingMessage + "]");
  }

  public static LogCapture captureLogEvents(Runnable codeBlock) {
    return new LogCapture(CaptureLogs.captureLogEvents(codeBlock));
  }
}
