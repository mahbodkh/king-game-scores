package com.king.scoreboard.handler;

import com.king.scoreboard.service.ServicesEnum;
import com.king.scoreboard.session.SessionManager;
import com.king.scoreboard.userscore.ScoreListUtil;
import com.king.scoreboard.userscore.UserScore;
import com.king.scoreboard.userscore.UserScoreManager;
import com.king.scoreboard.util.HttpCodes;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.logging.Logger;


public class UserScoreLevelService implements ServiceHandler {

    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private final int levelId;
    private final String sessionKey;
    private final String score;

    public UserScoreLevelService(int levelId, String sessionKey, String score) {
        this.levelId = levelId;
        this.sessionKey = sessionKey;
        this.score = score;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final long startTime = System.nanoTime();
        LOGGER.info(String.format("USER_SCORE_LEVEL Request ( levelId= %s )", levelId));

        if (ScoreListUtil.isScoreValidated(score)) {
            final String userId = SessionManager.getInstance().getUserSession(sessionKey).getUserId().toString();
            UserScoreManager.getInstance().putUserScore(levelId, new UserScore(userId, Integer.parseInt(score)));

            httpExchange.sendResponseHeaders(HttpCodes.OK.getCode(), 0);
            LOGGER.info(String.format("USER_SCORE_LEVEL Request ( levelId= %s , sessionKey= %s, score= %s ) " + "RETURNS: %s, takes: %s ms"
                    , levelId
                    , sessionKey
                    , score
                    , HttpCodes.OK
                    , (System.nanoTime() - startTime) / 1000000
            ));
        } else {
            httpExchange.sendResponseHeaders(HttpCodes.BAD_REQUEST.getCode(), -1);
            LOGGER.info(String.format("USER_SCORE_LEVEL Request ( levelId= %s , sessionKey= %s, score= %s ) " + "RETURNS: %s, takes: %s ms"
                    , levelId
                    , sessionKey
                    , score
                    , HttpCodes.BAD_REQUEST
                    , (System.nanoTime() - startTime) / 1000000
            ));
        }
    }

    @Override
    public ServicesEnum getService() {
        return ServicesEnum.USERSCORE_LEVEL;
    }

    @Override
    public String toString() {
        return "UserScoreLevelBaseHandler{" +
                "levelId=" + levelId +
                ", sessionKey='" + sessionKey + '\'' +
                ", score=" + score +
                '}';
    }
}
