package com.logcapture.assertion;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.logcapture.matcher.ExpectedExceptionMatcher;
import com.logcapture.matcher.TypedAnythingMatcher;
import org.hamcrest.Matcher;

public class ExpectedLoggedException {
  private Matcher<String> expectedMessageMatcher = new TypedAnythingMatcher<>();
  private Matcher<Exception> expectedException = new TypedAnythingMatcher<>();

  private ExpectedLoggedException() {
  }

  public static ExpectedLoggedException logException() {
    return new ExpectedLoggedException();
  }

  public ExpectedLoggedException withException(Matcher<? extends Exception> exceptionMatcher) {
    this.expectedException = new ExpectedExceptionMatcher(exceptionMatcher);
    return this;
  }

  public boolean matches(ILoggingEvent event) {
    if (event.getThrowableProxy() == null) {
      return false;
    }

    return expectedMessageMatcher.matches(event.getThrowableProxy().getMessage()) &&
      expectedException.matches(event.getThrowableProxy());
  }

  public ExpectedLoggedException withMessage(Matcher<String> expectedMessageMatcher) {
    this.expectedMessageMatcher = expectedMessageMatcher;
    return this;
  }

  @Override
  public String toString() {
    return "ExpectedLoggedException{" +
      "expectedMessageMatcher=" + expectedMessageMatcher +
      ", expectedException=" + expectedException +
      '}';
  }

  public static final ExpectedLoggedException ANYTHING = new ExpectedLoggedException() {
    public boolean matches(ILoggingEvent event) {
      return true;
    }
  };

}
