package com.logcapture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.logcapture.assertion.ExpectedLoggingMessage;

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

  public LogCapture<T> logged(ExpectedLoggingMessage expectedLoggingMessage) {
    for (ILoggingEvent event : events) {
      if (expectedLoggingMessage.matches(event)) {
        return this;
      }
    }

    throw new RuntimeException("No Log Found for [" + expectedLoggingMessage + "]");
  }

  public LogCapture<T> assertions(Consumer<T> assertions) {
    assertions.accept(result);
    return this;
  }

  public static LogCapture<Void> captureLogEvents(Runnable codeBlock) {
    return LogbackInterceptor.captureLogEvents(codeBlock);
  }

  public static <T> LogCapture<T> captureLogEvents(Supplier<T> codeBlock) {
    return LogbackInterceptor.captureLogEvents(codeBlock);
  }

  public static Await<Void> captureLogEventsAsync(Runnable codeBlock) {
    return (duration, condition) -> LogbackInterceptor.captureLogEventsAsync(codeBlock, duration, condition);
  }
  public static <T> Await<T> captureLogEventsAsync(Supplier<T> codeBlock) {
    return (duration, condition) -> LogbackInterceptor.captureLogEventsAsync(codeBlock, duration, condition);
  }
}
