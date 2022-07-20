package com.king.scoreboard.handler;

import com.king.scoreboard.properties.ConfigProperties;
import com.king.scoreboard.service.ServicesEnum;
import com.king.scoreboard.userscore.UserScoreManager;
import com.king.scoreboard.util.HttpCodes;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
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
        LOGGER.info(String.format("GET HIGH SCORE Request ( levelId= %s )", levelId));

        final String response = UserScoreManager.getInstance()
                .getHighScoreList(levelId, Integer.parseInt(ConfigProperties.MAX_HIGH_SCORES_RETURNED.getValue())
                );

        httpExchange.sendResponseHeaders(HttpCodes.OK.getCode(), response.length());
        httpExchange.getResponseBody().write(response.getBytes());

        LOGGER.info(String.format("GET-HIGH SCORE Request ( levelId= %s ) RETURNS: response= %s, takes: %s ms"
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
