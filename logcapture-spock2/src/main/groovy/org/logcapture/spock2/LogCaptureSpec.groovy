package org.logcapture.spock2

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import org.logcapture.LogCapture
import org.logcapture.logback.StubAppender
import org.hamcrest.Matcher
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static org.slf4j.Logger.ROOT_LOGGER_NAME

class LogCaptureSpec extends Specification {

  def root
  def logAppender

  def setup() {
    logAppender = new StubAppender()
    root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME)

    root.addAppender(logAppender)
  }

  def cleanup() {
    root.detachAppender(logAppender)
  }

  LogCaptureSpec logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage)
    return this
  }
  LogCaptureSpec logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage, Integer times) {
    new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage, times)
    return this
  }
}
