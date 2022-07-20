package com.king.scoreboard.session;

import java.util.Date;

/**
 * Model of the user session
 */
public class UserSession {
    private final Integer userId;
    private final String sessionKey;
    private Date createdDate;

    public UserSession(Integer userId, String sessionKey) {
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.createdDate = new Date();
    }

    public Integer getUserId() {
        return userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public Date getCreatedDate() {
        return (Date)createdDate.clone();
    }

    public void updateCreatedDate() {
        this.createdDate = new Date();
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userId=" + userId +
                ", sessionKey='" + sessionKey + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
