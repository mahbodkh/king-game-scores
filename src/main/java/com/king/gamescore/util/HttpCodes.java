package com.king.gamescore.util;

public enum HttpCodes {

    OK(200), BAD_REQUEST(400);

    private final int code;

    HttpCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return "HttpCode{" + name() + " " + code + '}';
    }
}
