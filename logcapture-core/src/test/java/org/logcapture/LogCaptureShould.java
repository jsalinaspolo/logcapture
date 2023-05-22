package org.logcapture;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.logcapture.assertion.ExpectedLoggingMessage;
import org.logcapture.assertion.VerificationException;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static ch.qos.logback.classic.Level.INFO;
import static org.logcapture.assertion.ExpectedLoggingMessage.aLog;

public class LogCaptureShould {

  @Test
  public void match_n_times() {
    LoggingEvent log1 = aLoggingEventWith(INFO, "message");

    LogCapture<ILoggingEvent> underTest = new LogCapture<>(Arrays.asList(log1, log1));

    underTest.logged(aLog().withMessage("message"), 2);
  }

  @Test
  public void match_n_times_filtering_others() {
    LoggingEvent log1 = aLoggingEventWith(INFO, "message");
    LoggingEvent log2 = aLoggingEventWith(INFO, "another");

    LogCapture<ILoggingEvent> underTest = new LogCapture<>(Arrays.asList(log1, log1, log2));

    ExpectedLoggingMessage expectedLog = aLog().withMessage("message");
    underTest.filter(expectedLog)
                    .logged(expectedLog, 2);
  }

  @Test
  public void match_n_times_multiples_messages() {
    LoggingEvent log1 = aLoggingEventWith(INFO, "message");
    LoggingEvent log2 = aLoggingEventWith(INFO, "another-message");

    LogCapture<ILoggingEvent> underTest = new LogCapture<>(Arrays.asList(log1, log2, log1, log2));

    underTest.logged(aLog().withMessage("message"), 4);
  }

  @Test
  public void fail_matching_n_times() {
    LoggingEvent log1 = aLoggingEventWith(INFO, "message");

    LogCapture<ILoggingEvent> underTest = new LogCapture<>(Arrays.asList(log1, log1));

    Assertions.assertThatThrownBy(
            () -> underTest.logged(aLog().withMessage("message"), 1)
        ).isInstanceOf(VerificationException.class)
        .hasMessageContaining("Expected 1 times but got 2:");

  }

  private LoggingEvent aLoggingEventWith(Level level, String message) {
    return aLoggingEventWith(level, message, null);
  }

  private LoggingEvent aLoggingEventWith(Level level, String message, Exception exception) {
    Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    return new LoggingEvent("fqcn", log, level, message, exception, null);
  }
}
