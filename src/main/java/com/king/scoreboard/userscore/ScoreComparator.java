package com.king.scoreboard.userscore;

import java.util.Comparator;

public class ScoreComparator implements Comparator<UserScore> {

    @Override
    public int compare(UserScore o1, UserScore o2) {
        return o1.getScore().compareTo(o2.getScore());
    }
}
