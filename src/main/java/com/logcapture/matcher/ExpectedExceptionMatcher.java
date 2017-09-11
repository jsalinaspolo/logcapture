package com.logcapture.matcher;

import ch.qos.logback.classic.spi.ThrowableProxy;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ExpectedExceptionMatcher extends BaseMatcher<Exception> {
  private final Matcher<? extends Exception> expectedException;

  public ExpectedExceptionMatcher(Matcher<? extends Exception> expectedException) {
    this.expectedException = expectedException;
  }

  @Override
  public boolean matches(Object item) {
    if (item instanceof ThrowableProxy) {
      return expectedException.matches(((ThrowableProxy) item).getThrowable());
    }

    return false;
  }

  @Override
  public void describeTo(Description description) {
    expectedException.describeTo(description);
  }
}
