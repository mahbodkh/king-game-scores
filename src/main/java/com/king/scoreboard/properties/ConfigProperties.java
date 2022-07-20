package com.king.scoreboard.properties;

public enum ConfigProperties {
    BASE_URI((getProperty("BASE_URI") != null) ? getProperty("BASE_URI") : "localhost"),
    LOGOUT_TIMEOUT((getProperty("LOGOUT_TIMEOUT") != null) ? getProperty("LOGOUT_TIMEOUT") : String.valueOf(10 * 60 * 1000)),
    LOGOUT_TIMEOUT_PERIOD_CHECK((getProperty("LOGOUT_TIMEOUT_PERIOD_CHECK") != null) ? getProperty("LOGOUT_TIMEOUT_PERIOD_CHECK") : String.valueOf(60 * 1000)),
    LOGOUT_TIMEOUT_PERIOD_DELAY((getProperty("LOGOUT_TIMEOUT_PERIOD_DELAY") != null) ? getProperty("LOGOUT_TIMEOUT_PERIOD_DELAY") : String.valueOf(0)),
    MAX_HIGH_SCORES_RETURNED((getProperty("MAX_HIGH_SCORES_RETURNED") != null) ? getProperty("MAX_HIGH_SCORES_RETURNED") : String.valueOf(15));

    private String value;

    ConfigProperties(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    private static String getProperty(String key) {
        return PropertiesManager.getInstance().getConfigProperty(key);
    }
}
