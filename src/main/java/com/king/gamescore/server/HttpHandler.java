package com.king.gamescore.server;

import com.king.gamescore.handler.ServiceHandler;
import com.king.gamescore.handler.HighScoreListService;
import com.king.gamescore.handler.LoginService;
import com.king.gamescore.handler.UserScoreLevelService;
import com.king.gamescore.service.ServicesEnum;
import com.king.gamescore.userscore.ScoreListUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by ioannis.metaxas on 2015-11-28.
 *
 * The http server handler for handling requests.
 * When a request has reached this point, it is sure that is a valid one.
 *
 */
public class HttpHandler implements com.sun.net.httpserver.HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        try {
            ServiceHandler serviceHandler = null;
            String requestedUri = httpExchange.getRequestURI().toString();
            int id = Integer.parseInt(requestedUri.split("/")[1]);
            if (ServicesEnum.isValidServiceUri(requestedUri, ServicesEnum.LOGIN)) {
                serviceHandler = new LoginService(id);
            } else if (ServicesEnum.isValidServiceUri(requestedUri, ServicesEnum.HIGHSCORE_LIST)) {
                serviceHandler = new HighScoreListService(id);
            } else if (ServicesEnum.isValidServiceUri(requestedUri, ServicesEnum.USERSCORE_LEVEL)) {
                String sessionKey = requestedUri.split("=")[1];
                String score = ScoreListUtil.getScore(httpExchange.getRequestBody());
                serviceHandler = new UserScoreLevelService(id, sessionKey, score);
            }
            Objects.requireNonNull(serviceHandler).handle(httpExchange);
        } finally {
            httpExchange.getResponseBody().close();
        }
    }
}
