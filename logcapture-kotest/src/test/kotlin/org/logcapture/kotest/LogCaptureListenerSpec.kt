package org.logcapture.kotest

import org.logcapture.assertion.ExpectedLoggingMessage.aLog
import io.kotest.core.spec.style.StringSpec
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
})
