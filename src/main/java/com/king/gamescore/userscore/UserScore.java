package com.king.gamescore.userscore;

import java.util.Comparator;


public class UserScore implements Comparator<UserScore> {

    @Override
    public int compare(UserScore o1, UserScore o2) {
        return o1.getScore().compareTo(o2.getScore());
    }

    private final String userId;
    private final Integer score;

    public UserScore(String userId, Integer score) {
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "UserScore{" +
                "userId='" + userId + '\'' +
                ", score=" + score +
                '}';
    }
}
