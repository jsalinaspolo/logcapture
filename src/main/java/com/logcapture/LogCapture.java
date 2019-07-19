package com.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.logcapture.assertion.VerificationException;
import org.hamcrest.Matcher;

import java.util.List;

public class LogCapture<T> {

  private final List<ILoggingEvent> events;

  public LogCapture(List<ILoggingEvent> events) {
    this.events = events;
  }

  public LogCapture<T> logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    if (expectedLoggingMessage.matches(events)) {
      return this;
    }

    throw VerificationException.forUnmatchedLog(expectedLoggingMessage, events);
  }
}
