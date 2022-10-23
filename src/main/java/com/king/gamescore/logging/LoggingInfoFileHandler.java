package com.king.gamescore.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * INFO Log level handler.
 */
public class LoggingInfoFileHandler extends FileHandler {

    public LoggingInfoFileHandler() throws IOException, SecurityException {
        super();
    }

    @Override
    public synchronized void publish(final LogRecord logRecord) {
        if (logRecord.getLevel().equals(Level.INFO)) {
            super.publish(logRecord);
        }
    }
}
