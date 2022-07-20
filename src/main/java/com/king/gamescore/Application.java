package com.king.gamescore;

import com.king.gamescore.properties.PropertiesManager;
import com.king.gamescore.server.HttpServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Application {

    private static final Logger LOGGER = Logger.getLogger("confLogger");

    public static void main(String[] args) {

        PropertiesManager.init();
        try {
            HttpServer server = new HttpServer();
            server.start();
            System.out.println("\nPress Enter to stop the server. ");
            System.in.read();
            server.stop();

            LOGGER.warning("\nScoreboard server stopped!");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Scoreboard server terminated unexpectedly!", ex);
        }
    }

}