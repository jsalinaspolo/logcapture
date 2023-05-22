package org.logcapture.spock2

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import org.hamcrest.Matcher
import org.logcapture.LogCapture
import org.logcapture.logback.StubAppender
import org.slf4j.LoggerFactory

trait LogCaptureTrait {

  def root
  def logAppender

  def setup() {
    logAppender = new StubAppender()
    root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)

    root.addAppender(logAppender)
  }

  def cleanup() {
    root.detachAppender(logAppender)
  }

  LogCapture logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    return new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage)
  }

  LogCapture logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage, Integer times) {
    return new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage, times)
  }
}
