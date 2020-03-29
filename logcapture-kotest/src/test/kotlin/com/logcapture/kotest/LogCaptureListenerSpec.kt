package com.logcapture.kotest

import com.logcapture.assertion.ExpectedLoggingMessage.aLog
import com.logcapture.kotest.LogCaptureListener.logged
import io.kotest.core.spec.style.StringSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogCaptureListenerSpec : StringSpec({

  val log: Logger = LoggerFactory.getLogger(LogCaptureListenerSpec::class.java)

  listener(LogCaptureListener)

  "verify log messages" {
    log.info("a message")

    logged(aLog().info().withMessage("a message"))
  }
})
