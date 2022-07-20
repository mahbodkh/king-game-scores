package com.king.scoreboard.server;

import com.sun.net.httpserver.HttpServer;

import java.util.Timer;
import java.util.logging.Logger;

/**
 * Server hook for terminating the server properly in case of an sudden shutdown.
 * Since it is a separate thread, it could also perform other actions like sending notifications to administrators.
 */
public class ShutdownHttpServer extends Thread {
    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private final HttpServer httpServer;
    private final Timer timer;

    public ShutdownHttpServer(HttpServer httpServer, Timer timer) {
        this.httpServer = httpServer;
        this.timer = timer;
    }

    public void run() {
        if (timer != null) {
            timer.cancel();
        }
        if (httpServer != null) {
            httpServer.stop(0);
        }
        LOGGER.warning("Shutting down the Scoreboard server...");
        LOGGER.info("Sending notifications to the system administrators...");
    }
}
