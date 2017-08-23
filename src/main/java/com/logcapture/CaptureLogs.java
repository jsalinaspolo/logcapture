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

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class CaptureLogs {

  public static LogCapture<Void> captureLogEvents(Runnable codeBlock) {
    StubAppender logAppender = new StubAppender();
    Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

    root.addAppender(logAppender);
    try {
      codeBlock.run();
      return new LogCapture<>(logAppender.events(), null);
    } finally {
      root.detachAppender(logAppender);
    }
  }

  public static <T> LogCapture<T> captureLogEvents(Supplier<T> codeBlock) {
    StubAppender logAppender = new StubAppender();
    Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

    root.addAppender(logAppender);
    try {
      T result = codeBlock.get();
      return new LogCapture<>(logAppender.events(), result);
    } finally {
      root.detachAppender(logAppender);
    }
  }

  public static <T> LogCapture<T> captureLogEventsAsync(Runnable codeBlock, Duration duration, ExpectedLoggingMessage expectedLoggingMessage) {
    StubAppender logAppender = new StubAppender();
    Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

    root.addAppender(logAppender);
    try {
      codeBlock.run();
      awaitForLogMessage(duration, hasLoggedMessage(logAppender.events(), expectedLoggingMessage));
      return new LogCapture<>(logAppender.events(), null);
    } finally {
      root.detachAppender(logAppender);
    }
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
