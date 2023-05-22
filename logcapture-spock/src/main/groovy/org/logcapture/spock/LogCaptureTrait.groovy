package org.logcapture.spock

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import org.logcapture.LogCapture
import org.logcapture.logback.StubAppender
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.slf4j.LoggerFactory

trait LogCaptureTrait {

  def root
  def logAppender

  @Before
  def setupLogger() {
    logAppender = new StubAppender()
    root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)

    root.addAppender(logAppender)
  }

  @After
  def detachAppender() {
    root.detachAppender(logAppender)
  }

  LogCapture logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    return new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage)
  }

  LogCapture filter(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    return new LogCapture<>(logAppender.events()).filter(expectedLoggingMessage)
  }
}
