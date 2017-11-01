package com.logcapture.assertion;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.logcapture.matcher.TypedAnythingMatcher;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.qos.logback.classic.Level.*;
import static org.hamcrest.Matchers.equalTo;

public class ExpectedLoggingMessage {

  private Matcher<Level> logLevelMatcher = new TypedAnythingMatcher<>();
  private Matcher<String> expectedMessageMatcher = new TypedAnythingMatcher<>();
  private Matcher<Integer> expectedLengthMatcher = new TypedAnythingMatcher<>();
  private Matcher<String> expectedLoggerNameMatcher = new TypedAnythingMatcher<>();
  private ExpectedLoggedException expectedLoggedException = ExpectedLoggedException.ANYTHING;
  private Map<String, Matcher<String>> mdcMatcher = new HashMap<>();

  private ExpectedLoggingMessage() {
  }

  public static ExpectedLoggingMessage aLog() {
    return new ExpectedLoggingMessage();
  }

  public ExpectedLoggingMessage withLevel(Matcher<Level> errorLevel) {
    this.logLevelMatcher = errorLevel;
    return this;
  }

  public ExpectedLoggingMessage debug() {
    this.logLevelMatcher = equalTo(DEBUG);
    return this;
  }

  public ExpectedLoggingMessage info() {
    this.logLevelMatcher = equalTo(INFO);
    return this;
  }

  public ExpectedLoggingMessage error() {
    this.logLevelMatcher = equalTo(ERROR);
    return this;
  }

  public boolean matches(ILoggingEvent event) {
    return
      logLevelMatcher.matches(event.getLevel()) &&
        expectedMessageMatcher.matches(event.getFormattedMessage()) &&
        expectedLoggedException.matches(event) &&
        expectedLoggerNameMatcher.matches(event.getLoggerName()) &&
        expectedLengthMatcher.matches(event.getFormattedMessage().length()) &&
        matchesMdc(event.getMDCPropertyMap());
  }

  private boolean matchesMdc(Map<String, String> mdcPropertyMap) {
    for (Map.Entry<String, Matcher<String>> entry : mdcMatcher.entrySet()) {
      String actualMdcValue = mdcPropertyMap.get(entry.getKey());
      if (!entry.getValue().matches(actualMdcValue)) {
        return false;
      }
    }

    return true;
  }

  public ExpectedLoggingMessage withMessage(Matcher<String> expectedMessage) {
    this.expectedMessageMatcher = expectedMessage;
    return this;
  }

  public ExpectedLoggingMessage withMessage(String expectedMessage) {
    this.expectedMessageMatcher = equalTo(expectedMessage);
    return this;
  }

  public ExpectedLoggingMessage withLoggerName(Matcher<String> expectedLoggerName) {
    this.expectedLoggerNameMatcher = expectedLoggerName;
    return this;
  }

  public ExpectedLoggingMessage havingException(ExpectedLoggedException expectedLoggedException) {
    this.expectedLoggedException = expectedLoggedException;
    return this;
  }

  public ExpectedLoggingMessage length(Matcher<Integer> expectedLengthMatcher) {
    this.expectedLengthMatcher = expectedLengthMatcher;
    return this;
  }

  public ExpectedLoggingMessage withMdc(String mdcKey, Matcher<String> mdcValue) {
    mdcMatcher.put(mdcKey, mdcValue);
    return this;
  }

  @Override
  public String toString() {
    return "ExpectedLoggingMessage{" + description() + '}';
  }

  private String description() {
    List<String> results = new ArrayList<>();

    results.addAll(toList("logLevelMatcher", logLevelMatcher));
    results.addAll(toList("expectedMessageMatcher", expectedMessageMatcher));
    results.addAll(toList("expectedLengthMatcher", expectedLengthMatcher));
    results.addAll(toList("expectedLoggerNameMatcher", expectedLoggerNameMatcher));
    results.addAll(toList("expectedMdc", mdcMatcher));

    if (!(expectedLoggedException == ExpectedLoggedException.ANYTHING)) {
      results.add("expectedLoggedException" + "=" + expectedLoggedException);
    }

    return results.stream().collect(Collectors.joining(", "));
  }

  private List<String> toList(String fieldName, Object field) {
    List<String> description = new ArrayList<>();
    if (!(field instanceof TypedAnythingMatcher)) {
      description.add(fieldName + "=" + field);
    }
    return description;
  }
}
