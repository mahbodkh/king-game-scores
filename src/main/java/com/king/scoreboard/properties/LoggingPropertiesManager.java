package com.king.scoreboard.properties;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingPropertiesManager {

    private static final java.util.logging.LogManager logManager = LogManager.getLogManager();
    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private static final String LOGGING_PROPERTIES = "logging.properties";

    private static volatile LoggingPropertiesManager instance = null;
    private final static Object lock = new Object();

    private LoggingPropertiesManager() {
        load();
    }

    /**
     * Creates or reuses the manager's instance.
     * Ensures that only a Singleton instance is used.
     *
     * @return the manager's instance.
     */
    public static LoggingPropertiesManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null)
                    instance = new LoggingPropertiesManager();
            }
        }
        return instance;
    }

    /**
     * Loads the logging properties from a file
     */
    private void load() {
        try {
            logManager.readConfiguration(this.getClass().getClassLoader().getResourceAsStream(LOGGING_PROPERTIES));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Couldn't load logging properties...", e);
        }
    }
}
