package org.logcapture.kotest

import io.kotest.core.spec.style.StringSpec
import org.logcapture.assertion.ExpectedLoggingMessage.aLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogCaptureListenerSpec : StringSpec({

  val log: Logger = LoggerFactory.getLogger(LogCaptureListenerSpec::class.java)

  val logCaptureListener = LogCaptureListener()
  listener(logCaptureListener)

  "verify log messages" {
    log.info("a message")

    logCaptureListener.logged(aLog().info().withMessage("a message"))
  }

  "verify log n times " {
    log.info("a message")
    log.info("a message")

    val expectedMessage = aLog().info().withMessage("a message")
    logCaptureListener.filter(expectedMessage).logged(expectedMessage, 2)
  }
})
