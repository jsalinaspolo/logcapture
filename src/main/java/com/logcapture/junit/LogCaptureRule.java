package com.logcapture.junit;

import ch.qos.logback.classic.Logger;
import com.logcapture.LogCapture;
import com.logcapture.assertion.ExpectedLoggingMessage;
import com.logcapture.infrastructure.logback.StubAppender;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.LoggerFactory;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class LogCaptureRule implements MethodRule, TestRule {

  private StubAppender logAppender;

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
        Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

        root.addAppender(logAppender);
        try {
          base.evaluate();
        } finally {
          root.detachAppender(logAppender);
        }
      }
    };
  }

  public void logged(ExpectedLoggingMessage expectedLoggingMessage) {
    new LogCapture<>(logAppender.events(), null).logged(expectedLoggingMessage);
  }
}
