package org.logcapture.kotest

import ch.qos.logback.classic.Level
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.logcapture.assertion.ExpectedLoggingMessage.aLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

class LogCaptureListenerSpec : StringSpec({

  val log: Logger = LoggerFactory.getLogger(LogCaptureListenerSpec::class.java)

  val logCaptureListener = LogCaptureListener()
  extensions(logCaptureListener)

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

  "verify using kotest assertions" {
    log.info("a message")

    logCaptureListener.logged().events.forOne {
      it.level shouldBe Level.INFO
      it.message shouldContain "message"
    }
  }
})

fun logMessageWhenCondition(log: Logger, condition: Boolean) {
  if (condition) {
    log.info("a message")
  }
}

