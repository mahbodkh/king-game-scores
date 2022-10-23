package com.king.gamescore.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * SERVER Log level handler.
 */
public class LoggingSevereFileHandler extends FileHandler {

    public LoggingSevereFileHandler() throws IOException, SecurityException {
        super();
    }

    @Override
    public synchronized void publish(final LogRecord logRecord) {
        if (logRecord.getLevel().equals(Level.SEVERE)) {
            super.publish(logRecord);
        }
    }
}
