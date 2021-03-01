package org.logcapture.kotest

import org.logcapture.assertion.ExpectedLoggingMessage.aLog
import io.kotest.core.spec.style.StringSpec
import org.hamcrest.Matchers.equalTo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogCaptureListenerWithCustomLoggerNameSpec : StringSpec({

  val log: Logger = LoggerFactory.getLogger("CUSTOM-LOGGER")

  val logCaptureListener = LogCaptureListener("CUSTOM-LOGGER")
  listener(logCaptureListener)

  "verify log messages" {
    log.info("a message")

    logCaptureListener.logged(aLog().info().withLoggerName(equalTo("CUSTOM-LOGGER")))
    logCaptureListener.logged(aLog().info().withMessage("a message"))
  }
})
