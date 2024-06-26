package org.logcapture.junit5;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.logcapture.LogCapture;
import org.logcapture.logback.StubAppender;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class LogCaptureExtension implements BeforeEachCallback, AfterEachCallback {

  private final String loggerName;
  private StubAppender logAppender;
  private Logger root;

  public LogCaptureExtension() {
    this(ROOT_LOGGER_NAME);
  }

  public LogCaptureExtension(String loggerName) {
    this.loggerName = loggerName;
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    logAppender = new StubAppender();
    root = (Logger) LoggerFactory.getLogger(loggerName);

    root.addAppender(logAppender);
  }

  @Override
  public void afterEach(ExtensionContext context) {
    root.detachAppender(logAppender);
  }

  public LogCapture logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    return new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage);
  }

  public LogCapture logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage, Integer times) {
    return new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage, times);
  }

  public List<ILoggingEvent> getEvents() {
    return logAppender.events();
  }
}
