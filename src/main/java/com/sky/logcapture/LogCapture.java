package com.sky.logcapture;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.sky.logcapture.infrastructure.logback.StubAppender;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class LogCapture {

  public static List<ILoggingEvent> captureLogEvents(Runnable codeBlock) {
    final StubAppender logAppender = new StubAppender();
    Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

    root.addAppender(logAppender);
    codeBlock.run();
    root.detachAppender(logAppender);

    return logAppender.events();
  }
}
