package com.king.gamescore.handler;

import com.king.gamescore.properties.ConfigProperties;
import com.king.gamescore.service.ServicesEnum;
import com.king.gamescore.userscore.UserScoreManager;
import com.king.gamescore.util.HttpCodes;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Logger;


/**
 * High Score Handler.
 */
public class HighScoreListService implements ServiceHandler {

    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private final int levelId;

    public HighScoreListService(int levelId) {
        this.levelId = levelId;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final long startTime = System.nanoTime();
        LOGGER.info(() -> MessageFormat.format("GET HIGH SCORE Request ( levelId= \"{0}\" )", levelId));

        final String response = UserScoreManager.getInstance()
                .getHighScoreList(levelId, Integer.parseInt(ConfigProperties.MAX_HIGH_SCORES_RETURNED.getValue()));

        httpExchange.sendResponseHeaders(HttpCodes.OK.getCode(), response.length());
        httpExchange.getResponseBody().write(response.getBytes());

        LOGGER.info(() -> MessageFormat.format("GET-HIGH SCORE Request ( levelId= \"{0}\" ) RETURNS: response= \"{1}\", takes: \"{2}\" ms"
                , levelId
                , response
                , (System.nanoTime() - startTime) / 1000000)
        );
    }

    @Override
    public ServicesEnum getService() {
        return ServicesEnum.HIGHSCORE_LIST;
    }
}
