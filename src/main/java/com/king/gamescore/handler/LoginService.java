package com.king.gamescore.handler;

import com.king.gamescore.service.ServicesEnum;
import com.king.gamescore.session.SessionManager;
import com.king.gamescore.util.HttpCodes;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.text.MessageFormat;
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
        LOGGER.info(() -> MessageFormat.format("LOGIN Request (userId= \"{0}\" )", userId));

        final String sessionKey = SessionManager.getInstance().createUserSession(userId).getSessionKey();
        httpExchange.sendResponseHeaders(HttpCodes.OK.getCode(), sessionKey.length());
        httpExchange.getResponseBody().write(sessionKey.getBytes());

        LOGGER.info(() -> MessageFormat.format("LOGIN Request ( userId=\"{0}\" ) RETURNS: sessionKey=\"{1}\", takes: \"{2}\" ms"
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
