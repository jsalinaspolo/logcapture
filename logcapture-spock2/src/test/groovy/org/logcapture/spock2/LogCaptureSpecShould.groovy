package org.logcapture.spock2


import org.slf4j.LoggerFactory
import spock.lang.Shared

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static org.logcapture.assertion.ExpectedLoggingMessage.aLog
import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not

class LogCaptureSpecShould extends LogCaptureSpec {

  @Shared log = LoggerFactory.getLogger(getClass())

  def "verify missing events"() {
    expect:
    logged(not(aLog()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("missing message"))))
  }

  def "verify multiple events"() {
    expect:
    log.info("first message")
    log.debug("second message")
    logged allOf(
        aLog().withLevel(INFO).withMessage("first message"),
        aLog().withLevel(DEBUG).withMessage("second message"))
  }

  def "verify sync logs using rule"() {
    expect:
    log.info("a message")

    logged(aLog().info().withMessage("a message"))
  }
}
