package com.king.gamescore.server;

import com.sun.net.httpserver.HttpServer;

import java.util.Timer;
import java.util.logging.Logger;

/**
 * Server hook for terminating the server properly in case of a sudden shutdown.
 * Since it is a separate thread, it could also perform other actions like sending notifications to administrators.
 */
public class ShutdownHttpServer extends Thread {
    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private final HttpServer server;
    private final Timer timer;

    public ShutdownHttpServer(HttpServer server, Timer timer) {
        this.server = server;
        this.timer = timer;
    }

    @Override
    public void run() {
        if (timer != null) {
            timer.cancel();
        }
        if (server != null) {
            server.stop(0);
        }
        LOGGER.warning("Shutting down the GameScore server...");
        LOGGER.info("Sending notifications to the system administrators...");
    }
}
