package org.logcapture.kotest

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import org.logcapture.LogCapture
import org.logcapture.logback.StubAppender
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.hamcrest.Matcher
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory

class LogCaptureListener(private val loggerName: String = ROOT_LOGGER_NAME) : TestListener {

  private lateinit var logAppender: StubAppender
  private lateinit var root: Logger

  override suspend fun beforeTest(testCase: TestCase) {
    logAppender = StubAppender()
    root = LoggerFactory.getLogger(loggerName) as Logger
    root.addAppender(logAppender)
  }

  override suspend fun afterTest(testCase: TestCase, result: TestResult) {
    root.detachAppender(logAppender)
  }

  fun logged(expectedLoggingMessage: Matcher<List<ILoggingEvent>>): LogCapture<Any> {
    return LogCapture<Any>(logAppender.events()).logged(expectedLoggingMessage)
  }

  fun logged(expectedLoggingMessage: Matcher<List<ILoggingEvent>>, times: Int): LogCapture<Any> {
    return LogCapture<Any>(logAppender.events()).logged(expectedLoggingMessage, times)
  }
}
