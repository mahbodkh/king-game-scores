package com.king.gamescore.handler;

import com.king.gamescore.service.ServicesEnum;
import com.king.gamescore.session.SessionManager;
import com.king.gamescore.userscore.ScoreListUtil;
import com.king.gamescore.userscore.UserScore;
import com.king.gamescore.userscore.UserScoreManager;
import com.king.gamescore.util.HttpCodes;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.text.MessageFormat;
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
        LOGGER.info(() -> MessageFormat.format("USER_SCORE_LEVEL Request ( levelId= \"{0}\" )", levelId));

        if (ScoreListUtil.isScoreValidated(score)) {
            final String userId = SessionManager.getInstance().getUserSession(sessionKey).getUserId().toString();
            UserScoreManager.getInstance().putUserScore(levelId, new UserScore(userId, Integer.parseInt(score)));

            httpExchange.sendResponseHeaders(HttpCodes.OK.getCode(), 0);
            responseLogger(startTime, HttpCodes.OK);
        } else {
            httpExchange.sendResponseHeaders(HttpCodes.BAD_REQUEST.getCode(), -1);
            responseLogger(startTime, HttpCodes.BAD_REQUEST);
        }
    }

    private void responseLogger(long startTime, HttpCodes status) {
        LOGGER.info(() -> MessageFormat.format("USER_SCORE_LEVEL Request ( levelId= \"{0}\" , sessionKey= \"{1}\", score= \"{2}\" ) " + "RETURNS: \"{3}\", takes: \"{4}\" ms"
                , levelId
                , sessionKey
                , score
                , status
                , (System.nanoTime() - startTime) / 1000000
        ));
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
