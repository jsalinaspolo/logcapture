package com.logcapture.spock2

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import com.logcapture.LogCapture
import com.logcapture.logback.StubAppender
import org.hamcrest.Matcher
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

  LogCaptureTrait logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage)
    return this
  }
}
