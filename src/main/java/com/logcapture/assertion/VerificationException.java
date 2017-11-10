package com.logcapture.assertion;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.List;
import java.util.stream.Collectors;

public class VerificationException extends AssertionError {

  private VerificationException(String message) {
    super(message);
  }

  public static VerificationException forUnmatchedLog(ExpectedLoggingMessage expectedLogMessage, List<ILoggingEvent> logEvents) {
    return new VerificationException(String.format(
      "Expected at least one log matching: \n%s\nLogs received: \n%s",
      expectedLogMessage.toString(),
      logEvents.stream()
        .map(VerificationException::formatLogEvent)
        .collect(Collectors.joining("\n"))
    ));
  }

  private static String formatLogEvent(ILoggingEvent log) {
    return String.format("level: %s mdc: %s message: %s", log.getLevel(),
      log.getMDCPropertyMap(),
      log.getFormattedMessage());
  }
}
