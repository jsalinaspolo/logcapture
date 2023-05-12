package org.logcapture.junit4;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.logcapture.LogCapture;
import org.logcapture.logback.StubAppender;
import org.hamcrest.Matcher;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class LogCaptureRule implements MethodRule, TestRule {

  private final String loggerName;
  private StubAppender logAppender;

  public LogCaptureRule() {
    this(ROOT_LOGGER_NAME);
  }

  public LogCaptureRule(String loggerName) {
    this.loggerName = loggerName;
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return apply(base, null, null);
  }

  @Override
  public Statement apply(Statement base, FrameworkMethod method, Object target) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        logAppender = new StubAppender();
        Logger root = (Logger) LoggerFactory.getLogger(loggerName);

        root.addAppender(logAppender);
        try {
          base.evaluate();
        } finally {
          root.detachAppender(logAppender);
        }
      }
    };
  }

  public LogCaptureRule logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage);
    return this;
  }

  public LogCaptureRule logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage, Integer times) {
    new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage, times);
    return this;
  }
}
