package com.king.gamescore.properties;


public class PropertiesManager {

    private static volatile PropertiesManager instance = null;
    private static final Object lock = new Object();

    private final LoggingPropertiesManager loggingPropertiesManager;
    private final ServerPropertiesManager serverPropertiesManager;
    private final ConfigurationManager configurationManager;

    private PropertiesManager(){
        loggingPropertiesManager = LoggingPropertiesManager.getInstance();
        serverPropertiesManager = ServerPropertiesManager.getInstance();
        configurationManager = ConfigurationManager.getInstance();
    }

    /**
     * Creates or reuses the manager's instance.
     * Ensures that only a Singleton instance is used.
     *
     * @return the manager's instance.
     */
    public static PropertiesManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null)
                    instance = new PropertiesManager();
            }
        }
        return instance;
    }

    public static void init() {
        getInstance();
    }

    /**
     * Returns a server property from the given key
     * @param key which corresponds to a server property
     * @return a server property from the given key
     */
    public String getServerProperty(String key) {
        return serverPropertiesManager.getProperty(key);
    }

    /**
     * Returns a configuration property from the given key
     * @param key which corresponds to a configuration property
     * @return a configuration property from the given key
     */
    public String getConfigProperty(String key) {
        return configurationManager.getProperty(key);
    }
}
