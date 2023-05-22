package org.logcapture.kotest

import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.StringSpec
import org.logcapture.assertion.ExpectedLoggingMessage.aLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

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

    logCaptureListener.logged(aLog().info().withMessage("a message"), 2)
  }

  "use eventually to verify logs" {
    var i = 0
    eventually(1.seconds) {
      i += 1
      logMessageWhenCondition(log, i == 5)
      logCaptureListener.logged(aLog().info().withMessage("a message"))
    }
  }
})

fun logMessageWhenCondition(log: Logger, condition: Boolean) {
  if (condition) {
    log.info("a message")
  }
}

