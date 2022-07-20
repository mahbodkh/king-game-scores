package com.king.gamescore.handler;

import com.king.gamescore.service.ServicesEnum;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface ServiceHandler {

    ServicesEnum getService();

    void handle(HttpExchange httpExchange) throws IOException;
}
