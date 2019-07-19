package com.logcapture.spock

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import com.logcapture.LogCapture
import com.logcapture.infrastructure.logback.StubAppender
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static org.slf4j.Logger.ROOT_LOGGER_NAME

class LogCaptureSpec extends Specification {

  def root
  def logAppender

  @Before
  def setupLogger() {
    logAppender = new StubAppender()
    root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME)

    root.addAppender(logAppender)
  }

  @After
  def detachAppender() {
    root.detachAppender(logAppender)
  }

  LogCaptureSpec logged(Matcher<List<ILoggingEvent>> expectedLoggingMessage) {
    new LogCapture<>(logAppender.events()).logged(expectedLoggingMessage)
    return this
  }
}
