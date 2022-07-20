package com.king.gamescore.properties;

public enum ServerProperties {

    SERVER_PORT((getProperty("SERVER_PORT") != null) ? Integer.parseInt(getProperty("SERVER_PORT")) : 8081),
    SERVER_BACKLOG((getProperty("SERVER_BACKLOG") != null) ? Integer.parseInt(getProperty("SERVER_BACKLOG")) : 0),
    HTTP_POOL_CONNECTIONS((getProperty("HTTP_POOL_CONNECTIONS") != null) ? Integer.parseInt(getProperty("HTTP_POOL_CONNECTIONS")) : Runtime.getRuntime().availableProcessors() * 20),
    DELAY_FOR_TERMINATION((getProperty("DELAY_FOR_TERMINATION") != null) ? Integer.parseInt(getProperty("DELAY_FOR_TERMINATION")) : 0),
    HTTP_MAX_CONNECTIONS(HTTP_POOL_CONNECTIONS.getValue() * 2),
    HTTP_QUEUE_MAX_ITEMS(HTTP_POOL_CONNECTIONS.getValue() * 4);

    private final int value;

    ServerProperties(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    private static String getProperty(String key) {
        return PropertiesManager.getInstance().getServerProperty(key);
    }
}
