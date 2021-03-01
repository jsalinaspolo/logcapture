package org.logcapture.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StubAppender implements Appender<ILoggingEvent> {
  public static final String STUB_APPENDER_NAME = "stub-appender";
  private final List<ILoggingEvent> loggedEvents = new CopyOnWriteArrayList<>();

  @Override
  public String getName() {
    return STUB_APPENDER_NAME;
  }

  @Override
  public void doAppend(ILoggingEvent iLoggingEvent) throws LogbackException {
    loggedEvents.add(iLoggingEvent);
  }

  @Override
  public void setName(String s) {
  }

  @Override
  public void setContext(Context context) {
  }

  @Override
  public Context getContext() {
    return null;
  }

  @Override
  public void addStatus(Status status) {
  }

  @Override
  public void addInfo(String s) {
  }

  @Override
  public void addInfo(String s, Throwable throwable) {
  }

  @Override
  public void addWarn(String s) {
  }

  @Override
  public void addWarn(String s, Throwable throwable) {
  }

  @Override
  public void addError(String s) {
  }

  @Override
  public void addError(String s, Throwable throwable) {
  }

  @Override
  public void addFilter(Filter<ILoggingEvent> filter) {

  }

  @Override
  public void clearAllFilters() {
    loggedEvents.clear();
  }

  @Override
  public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
    return null;
  }

  @Override
  public FilterReply getFilterChainDecision(ILoggingEvent iLoggingEvent) {
    return null;
  }

  @Override
  public void start() {
  }

  @Override
  public void stop() {
  }

  @Override
  public boolean isStarted() {
    return true;
  }

  public List<ILoggingEvent> events() {
    return loggedEvents;
  }
}
