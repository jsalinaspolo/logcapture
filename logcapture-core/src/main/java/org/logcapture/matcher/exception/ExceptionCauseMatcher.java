package org.logcapture.matcher.exception;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ExceptionCauseMatcher extends BaseMatcher<Exception> {

  private final Class<? extends Exception> exceptionClass;

  private ExceptionCauseMatcher(Class<? extends Exception> exceptionClass) {
    this.exceptionClass = exceptionClass;
  }

  public static Matcher<Exception> causeOf(Class<? extends Exception> exceptionClass) {
    return new ExceptionCauseMatcher(exceptionClass);
  }

  @Override
  public boolean matches(Object exception) {
    Throwable expectedExceptionCause = ((Throwable) exception).getCause();
    return exceptionClass.isInstance(expectedExceptionCause);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Expecting exception to be instance of " + exceptionClass);
  }
}
