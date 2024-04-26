package org.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hamcrest.Matcher;
import org.logcapture.assertion.VerificationException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public LogCapture<T> logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage, Integer times) {
        logged(expectedLoggingMessage);
      List<ILoggingEvent> matchingEvents = events.stream()
          .filter(actual -> expectedLoggingMessage.matches(Collections.singletonList(actual)))
          .collect(Collectors.toList());
      if (matchingEvents.size() != times) {
            throw VerificationException.forUnmatchedTimesLog(expectedLoggingMessage, events, times, events.size());
        }
        return this;
    }

    public List<ILoggingEvent> getEvents() {
      return Collections.unmodifiableList(events);
    }
}
