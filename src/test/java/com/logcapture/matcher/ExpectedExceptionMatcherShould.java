package com.logcapture.matcher;

import ch.qos.logback.classic.spi.ThrowableProxy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.isA;

public class ExpectedExceptionMatcherShould {

  @Test
  public void match_when_ThrowableProxy_contains_exception() {
    ExpectedExceptionMatcher expectedExceptionMatcher = new ExpectedExceptionMatcher(isA(RuntimeException.class));

    boolean matches = expectedExceptionMatcher.matches(new ThrowableProxy(new RuntimeException()));

    assertThat(matches).isTrue();
  }

  @Test
  public void not_match_when_is_not_ThrowableProxy() {
    ExpectedExceptionMatcher expectedExceptionMatcher = new ExpectedExceptionMatcher(isA(RuntimeException.class));

    boolean matches = expectedExceptionMatcher.matches(new RuntimeException());

    assertThat(matches).isFalse();
  }

  @Test
  public void not_match_when_ThrowableProxy_contains_another_exception() {
    ExpectedExceptionMatcher expectedExceptionMatcher = new ExpectedExceptionMatcher(isA(IllegalArgumentException.class));

    boolean matches = expectedExceptionMatcher.matches(new ThrowableProxy(new IllegalStateException()));

    assertThat(matches).isFalse();
  }


}
