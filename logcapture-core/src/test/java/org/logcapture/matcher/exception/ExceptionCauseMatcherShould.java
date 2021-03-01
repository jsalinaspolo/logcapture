package org.logcapture.matcher.exception;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.logcapture.matcher.exception.ExceptionCauseMatcher.causeOf;
import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionCauseMatcherShould {

  @Test
  public void match_when_cause_matches() {
    Matcher<Exception> matcher = causeOf(IllegalStateException.class);

    assertThat(matcher.matches(new Throwable(new IllegalStateException()))).isTrue();
  }

  @Test
  public void not_match_when_cause_different() {
    Matcher<Exception> matcher = causeOf(IllegalStateException.class);

    assertThat(matcher.matches(new Throwable(new IllegalArgumentException()))).isFalse();
  }

  @Test
  public void description_adds_context() {
    Matcher<Exception> matcher = causeOf(RuntimeException.class);
    StringDescription description = new StringDescription();

    matcher.describeTo(description);

    assertThat(description.toString()).isEqualTo("Expecting exception to be instance of class java.lang.RuntimeException");
  }
}
