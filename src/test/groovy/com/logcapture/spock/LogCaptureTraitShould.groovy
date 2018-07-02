package com.logcapture.spock

import com.logcapture.junit.LogCaptureRuleShould
import com.logcapture.junit.LogCaptureTrait
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Specification

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.INFO
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog
import static com.logcapture.assertion.ExpectedLoggingMessage.aLog
import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not

class LogCaptureTraitShould extends Specification implements LogCaptureTrait {


  private static final String LOG_NAME = "aLogNotAttachedToRoot"
  @Shared log = LoggerFactory.getLogger(LogCaptureRuleShould.class)

  def "verify_missing_events"() {
    expect:
    logged(not(aLog()
        .withLevel(equalTo(INFO))
        .withMessage(equalTo("missing message"))))
  }

  def "verify_multiple_events"() {
    expect:
    log.info("first message")
    log.debug("second message")
    logged(allOf(aLog().withLevel(equalTo(INFO)).withMessage(equalTo("first message")),
        aLog().withLevel(equalTo(DEBUG)).withMessage(equalTo("second message"))))
  }

  def "verify_sync_logs_using_rule"() {
    expect:
    log.info("a message")

    logged(aLog().info().withMessage("a message"))
  }

//  @Test
//  void verify_log_when_is_not_in_root() {
//    Logger logNotInRoot = createLogger(LOG_NAME)
//
//    logNotInRoot.info("a message")
//
//    assertThatExceptionOfType(VerificationException.class)
//        .isThrownBy(() -> logCaptureRule.logged(aLog().info().withMessage("a message")))
//
//    logCaptureRuleAttached.logged(aLog().info().withMessage("a message"))
//  }

  private Logger createLogger(String name) {
    ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(name)
    logger.setLevel(DEBUG)
    logger.setAdditive(false)
    return logger
  }

}
