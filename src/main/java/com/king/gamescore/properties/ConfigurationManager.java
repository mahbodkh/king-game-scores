package com.king.gamescore.properties;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationManager {
    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private static final String CONFIGURATION_PROPERTIES = "configuration.properties";

    private static volatile ConfigurationManager instance = null;
    private static final Object lock = new Object();

    private final Properties properties;

    private ConfigurationManager() {
        properties = new Properties();
        load();
    }

    /**
     * Creates or reuses the manager's instance.
     * Ensures that only a Singleton instance is used.
     *
     * @return the manager's instance.
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null)
                    instance = new ConfigurationManager();
            }
        }
        return instance;
    }

    /**
     * Loads the configuration properties from a file
     */
    void load() {
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(CONFIGURATION_PROPERTIES));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Couldn't load configuration properties...", e);
        }
    }

    protected String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}
