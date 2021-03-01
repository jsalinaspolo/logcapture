package org.logcapture.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class TypedAnythingMatcher<T> extends BaseMatcher<T> {

  @Override
  public boolean matches(Object item) {
    return true;
  }

  @Override
  public void describeTo(Description description) {
  }
}
