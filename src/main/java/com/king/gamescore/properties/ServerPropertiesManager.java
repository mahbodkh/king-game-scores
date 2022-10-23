package com.king.gamescore.properties;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerPropertiesManager {

    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private static final String SERVER_PROPERTIES = "server.properties";

    private static volatile ServerPropertiesManager instance = null;
    private static final Object lock = new Object();

    private final Properties properties;

    private ServerPropertiesManager() {
        properties = new Properties();
        load();
    }

    /**
     * Creates or reuses the manager's instance.
     * Ensures that only a Singleton instance is used.
     *
     * @return the manager's instance.
     */
    public static ServerPropertiesManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null)
                    instance = new ServerPropertiesManager();
            }
        }
        return instance;
    }

    /**
     * Loads the server properties from a file
     */
    void load() {
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(SERVER_PROPERTIES));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> "Couldn't load server properties...");
        }
    }

    protected String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}
