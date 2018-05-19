package com.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.logcapture.assertion.ExpectedLoggingMessage;
import com.logcapture.assertion.VerificationException;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LogCapture<T> {

  private final List<ILoggingEvent> events;
  private final T result;

  public LogCapture(List<ILoggingEvent> events, T result) {
    this.events = events;
    this.result = result;
  }

  public LogCapture<T> logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
      if (expectedLoggingMessage.matches(events)) {
        return this;
    }

    throw VerificationException.forUnmatchedLog(expectedLoggingMessage, events);
  }

  public LogCapture<T> assertions(Consumer<T> assertions) {
    assertions.accept(result);
    return this;
  }

  public static LogCapture<Void> captureLogEvents(Runnable codeBlock) {
    return LogbackInterceptor.captureLogEvents(codeBlock);
  }

  public static LogCapture<Void> captureLogEvents(Runnable codeBlock, String loggerName) {
    return LogbackInterceptor.captureLogEvents(codeBlock, loggerName);
  }

  public static <T> LogCapture<T> captureLogEvents(Supplier<T> codeBlock) {
    return LogbackInterceptor.captureLogEvents(codeBlock);
  }

  public static <T> LogCapture<T> captureLogEvents(Supplier<T> codeBlock, String loggerName) {
    return LogbackInterceptor.captureLogEvents(codeBlock, loggerName);
  }

  public static Await<Void> captureLogEventsAsync(Runnable codeBlock) {
    return (duration, condition) -> LogbackInterceptor.captureLogEventsAsync(codeBlock, duration, condition);
  }

  public static <T> Await<T> captureLogEventsAsync(Supplier<T> codeBlock) {
    return (duration, condition) -> LogbackInterceptor.captureLogEventsAsync(codeBlock, duration, condition);
  }
}
