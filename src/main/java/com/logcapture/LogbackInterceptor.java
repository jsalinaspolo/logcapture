package com.logcapture;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.logcapture.assertion.ExpectedLoggingMessage;
import com.logcapture.infrastructure.logback.StubAppender;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

class LogbackInterceptor {

  public static LogCapture<Void> captureLogEvents(Runnable codeBlock) {
    return captureLogEvents(codeBlock, ROOT_LOGGER_NAME);
  }

  public static LogCapture<Void> captureLogEvents(Runnable codeBlock, String loggerName) {
    return captureLogEvents(wrapRunnable(codeBlock), loggerName);
  }

  public static <T> LogCapture<T> captureLogEvents(Supplier<T> codeBlock) {
    return captureLogEvents(codeBlock, ROOT_LOGGER_NAME);
  }

  public static <T> LogCapture<T> captureLogEvents(Supplier<T> codeBlock, String loggerName) {
    StubAppender logAppender = new StubAppender();
    Logger root = (Logger) LoggerFactory.getLogger(loggerName);

    root.addAppender(logAppender);
    try {
      T result = codeBlock.get();
      return new LogCapture<>(logAppender.events(), result);
    } finally {
      root.detachAppender(logAppender);
    }
  }

  public static LogCapture<Void> captureLogEventsAsync(Runnable codeBlock, Duration duration, ExpectedLoggingMessage expectedLoggingMessage) {
    return captureLogEventsAsync(wrapRunnable(codeBlock), duration, expectedLoggingMessage);
  }

  public static <T> LogCapture<T> captureLogEventsAsync(Supplier<T> codeBlock, Duration duration, ExpectedLoggingMessage expectedLoggingMessage) {
    StubAppender logAppender = new StubAppender();
    Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

    root.addAppender(logAppender);
    try {
      T result = codeBlock.get();
      awaitForLogMessage(duration, hasLoggedMessage(logAppender.events(), expectedLoggingMessage));
      return new LogCapture<>(logAppender.events(), result);
    } finally {
      root.detachAppender(logAppender);
    }
  }

  private static Supplier<Void> wrapRunnable(Runnable codeBlock) {
    return () -> {
      codeBlock.run();
      return null;
    };
  }

  private static Callable<Boolean> hasLoggedMessage(List<ILoggingEvent> events, ExpectedLoggingMessage expectedLoggingMessage) {
    return () -> {
      try {
        new LogCapture<>(events, null).logged(expectedLoggingMessage);
        return true;
      } catch (Exception e) {
        return false;
      }
    };
  }

  private static void awaitForLogMessage(Duration duration, Callable<Boolean> condition) {
    await().atMost(duration.getSeconds(), SECONDS).until(condition);
  }
}
