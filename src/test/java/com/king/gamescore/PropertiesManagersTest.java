package com.king.gamescore;

import com.king.gamescore.properties.ConfigurationManager;
import com.king.gamescore.properties.LoggingPropertiesManager;
import com.king.gamescore.properties.PropertiesManager;
import com.king.gamescore.properties.ServerPropertiesManager;
import org.junit.Test;

import static org.junit.Assert.assertSame;


public class PropertiesManagersTest {

    @Test
    public void testSingletonInstances() {
        assertSame(PropertiesManager.getInstance(), PropertiesManager.getInstance());
        assertSame(ServerPropertiesManager.getInstance(), ServerPropertiesManager.getInstance());
        assertSame(LoggingPropertiesManager.getInstance(), LoggingPropertiesManager.getInstance());
        assertSame(ConfigurationManager.getInstance(), ConfigurationManager.getInstance());
    }
}
