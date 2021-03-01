package org.logcapture.matcher.exception;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ExceptionCauseMessageMatcher extends BaseMatcher<Exception> {

  private final Matcher<String> causeMatcher;

  private ExceptionCauseMessageMatcher(Matcher<String> causeMatcher) {
    this.causeMatcher = causeMatcher;
  }

  public static Matcher<Exception> whereCauseMessage(Matcher<String> causeMatcher) {
    return new ExceptionCauseMessageMatcher(causeMatcher);
  }

  @Override
  public boolean matches(Object exception) {
    final Throwable expectedExceptionCause = ((Throwable) exception).getCause();
    return causeMatcher.matches(expectedExceptionCause.getMessage());
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Expecting exception cause to contain " + causeMatcher);
  }
}
