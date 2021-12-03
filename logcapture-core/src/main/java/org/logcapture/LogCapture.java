package org.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.logcapture.assertion.VerificationException;
import org.hamcrest.Matcher;

import java.util.List;

public class LogCapture<T> {


  private final ListAppender<ILoggingEvent> events;

  public LogCapture(ListAppender<ILoggingEvent> events) {
    this.events = events;
  }

  public LogCapture<T> logged(Matcher<ListAppender<ILoggingEvent>> expectedLoggingMessage) {
    if (expectedLoggingMessage.matches(events)) {
      return this;
    }

    throw VerificationException.forUnmatchedLog(expectedLoggingMessage, events);
  }
}
