package com.king.gamescore.service;

import com.king.gamescore.util.HttpCodes;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.logging.Logger;

public class ServiceFilter extends Filter {

    private static final Logger LOGGER = Logger.getLogger("confLogger");

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        if(ServicesEnum.isValidService(httpExchange.getRequestURI().toString())){
            chain.doFilter(httpExchange);
        } else {
            LOGGER.warning("No service for the request: " + httpExchange.getRequestURI().toString());
            httpExchange.sendResponseHeaders(HttpCodes.BAD_REQUEST.getCode(), -1);
            httpExchange.getResponseBody().close();
        }
    }

    @Override
    public String description() {
        return "Filters the requests based on the supported services";
    }
}
