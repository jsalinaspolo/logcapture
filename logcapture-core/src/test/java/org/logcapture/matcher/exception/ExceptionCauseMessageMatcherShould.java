package org.logcapture.matcher.exception;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.logcapture.matcher.exception.ExceptionCauseMessageMatcher.whereCauseMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class ExceptionCauseMessageMatcherShould {

  @Test
  public void match_when_message_matches() {
    Matcher<Exception> matcher = whereCauseMessage(equalTo("message"));

    assertThat(matcher.matches(new Throwable(new RuntimeException("message")))).isTrue();
  }

  @Test
  public void not_match_when_message_different() {
    Matcher<Exception> matcher = whereCauseMessage(equalTo("message"));

    assertThat(matcher.matches(new Throwable(new RuntimeException("different message")))).isFalse();
  }

  @Test
  public void description_adds_context() {
    Matcher<Exception> matcher = whereCauseMessage(equalTo("message"));
    StringDescription description = new StringDescription();

    matcher.describeTo(description);

    assertThat(description.toString()).isEqualTo("Expecting exception cause to contain \"message\"");
  }
}
