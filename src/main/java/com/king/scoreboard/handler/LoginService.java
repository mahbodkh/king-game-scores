package com.king.scoreboard.handler;

import com.king.scoreboard.service.ServicesEnum;
import com.king.scoreboard.session.SessionManager;
import com.king.scoreboard.util.HttpCodes;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Handles the login requests.
 */
public class LoginService implements ServiceHandler {

    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private final int userId;

    public LoginService(int userId) {
        this.userId = userId;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final long startTime = System.nanoTime();
        LOGGER.info(String.format("LOGIN Request (userId= %s )", userId));

        final String sessionKey = SessionManager.getInstance().createUserSession(userId).getSessionKey();
        httpExchange.sendResponseHeaders(HttpCodes.OK.getCode(), sessionKey.length());
        httpExchange.getResponseBody().write(sessionKey.getBytes());

        LOGGER.info(String.format("LOGIN Request ( userId=%s ) RETURNS: sessionKey= %s, takes: %s ms"
                , userId
                , sessionKey
                , (System.nanoTime() - startTime) / 1000000
        ));
    }

    @Override
    public ServicesEnum getService() {
        return ServicesEnum.LOGIN;
    }
}
