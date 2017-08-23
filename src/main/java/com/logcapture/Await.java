package com.logcapture;

import com.logcapture.assertion.ExpectedLoggingMessage;

import java.time.Duration;

@FunctionalInterface
public interface Await<T> {

  LogCapture<T> waitAtMost(Duration duration, ExpectedLoggingMessage condition);
}
