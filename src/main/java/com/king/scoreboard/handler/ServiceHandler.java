package com.king.scoreboard.handler;

import com.king.scoreboard.service.ServicesEnum;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface ServiceHandler {

    ServicesEnum getService();

    void handle(HttpExchange httpExchange) throws IOException;
}
