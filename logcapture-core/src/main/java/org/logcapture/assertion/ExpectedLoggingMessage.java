package org.logcapture.assertion;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.logcapture.matcher.TypedAnythingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.INFO;
import static ch.qos.logback.classic.Level.WARN;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.equalTo;

public class ExpectedLoggingMessage extends TypeSafeMatcher<List<ILoggingEvent>> {

  private Matcher<Level> logLevelMatcher = new TypedAnythingMatcher<>();
  private Matcher<Marker> markerMatcher = new TypedAnythingMatcher<>();
  private List<Matcher<String>> expectedMessageMatcher = new ArrayList<>();
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
    logLevelMatcher = errorLevel;
    return this;
  }

  public ExpectedLoggingMessage withLevel(Level errorLevel) {
    return withLevel(equalTo(errorLevel));
  }

  public ExpectedLoggingMessage withMarker(Matcher<Marker> marker) {
    markerMatcher = marker;
    return this;
  }

  @Override
  protected boolean matchesSafely(List<ILoggingEvent> events) {
    return events.stream().anyMatch(this::matches);
  }

  private boolean matches(ILoggingEvent event) {
    return logLevelMatcher.matches(event.getLevel()) &&
        markerMatcher.matches(event.getMarker()) &&
        expectedMessageMatcher.stream().allMatch(matcher -> matcher.matches(event.getFormattedMessage())) &&
        expectedLoggedException.matches(event) &&
        expectedLoggerNameMatcher.matches(event.getLoggerName()) &&
        expectedLengthMatcher.matches(event.getFormattedMessage().length()) &&
        matchesMdc(event.getMDCPropertyMap());
  }

  public ExpectedLoggingMessage withMarker(Marker marker) {
    return withMarker(equalTo(marker));
  }

  public ExpectedLoggingMessage withMarker(String marker) {
    return withMarker(MarkerFactory.getMarker(marker));
  }

  public ExpectedLoggingMessage debug() {
    logLevelMatcher = equalTo(DEBUG);
    return this;
  }

  public ExpectedLoggingMessage info() {
    logLevelMatcher = equalTo(INFO);
    return this;
  }

  public ExpectedLoggingMessage warn() {
    logLevelMatcher = equalTo(WARN);
    return this;
  }

  public ExpectedLoggingMessage error() {
    logLevelMatcher = equalTo(ERROR);
    return this;
  }

  private boolean matchesMdc(Map<String, String> mdcPropertyMap) {
    return mdcMatcher.entrySet().stream()
        .allMatch(entry -> entry.getValue().matches(mdcPropertyMap.get(entry.getKey())));
  }

  public final ExpectedLoggingMessage withMessage(Matcher<String> expectedMessages) {
    return withMessage(singleton(expectedMessages));
  }

  @SafeVarargs
  public final ExpectedLoggingMessage withMessage(Matcher<String>... expectedMessages) {
    return withMessage(Arrays.asList(expectedMessages));
  }

  public final ExpectedLoggingMessage withMessage(Collection<Matcher<String>> expectedMessages) {
    expectedMessageMatcher.addAll(expectedMessages);
    return this;
  }

  public ExpectedLoggingMessage withMessage(String expectedMessage) {
    return withMessage(equalTo(expectedMessage));
  }

  public ExpectedLoggingMessage withLoggerName(Matcher<String> expectedLoggerName) {
    expectedLoggerNameMatcher = expectedLoggerName;
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

  public ExpectedLoggingMessage withMdc(String mdcKey, String mdcValue) {
    withMdc(mdcKey, equalTo(mdcValue));
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
    results.addAll(toList("markerMatcher", markerMatcher));
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

  @Override
  public void describeTo(Description description) {
    description.appendText(description());
  }
}
