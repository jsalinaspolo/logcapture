package com.logcapture;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.logcapture.infrastructure.logback.StubAppender.STUB_APPENDER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class LogbackInterceptorShould {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Test
  public void detach_appender_when_events_are_captured() {
    LogbackInterceptor.captureLogEvents(() -> log.info("a message"));
    assertStubAppenderIsDetached();
  }

  @Test
  public void detach_appender_when_capturing_events_an_exception_is_thrown() {
    assertThatThrownBy(() -> LogbackInterceptor.captureLogEvents(() -> {
      log.info("a message");
      throw new IllegalStateException();
    })).isInstanceOf(IllegalStateException.class);

    assertStubAppenderIsDetached();
  }

  private void assertStubAppenderIsDetached() {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
    assertThat(root.getAppender(STUB_APPENDER_NAME)).isNull();
  }
}
