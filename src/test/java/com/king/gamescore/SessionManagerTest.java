package com.king.gamescore;

import com.king.gamescore.session.SessionManager;
import com.king.gamescore.session.UserSession;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;


public class SessionManagerTest {

    private SessionManager sessionManager;

    private static SecureRandom random;

    @Before
    public void setUp() {
        sessionManager = SessionManager.getInstance();
        random = new SecureRandom();
    }

    @Test
    public void testSingletonInstance() {
        assertSame(SessionManager.getInstance(), SessionManager.getInstance());
    }

    @Test
    public void testCreateUserSessionLoggedInUser() {
        // Login a user
        int userId = getRandomUserId();
        String sessionKey = getRandomSessionKey();
        UserSession userSession = new UserSession(userId, sessionKey);
        Date oldCreatedDate = userSession.getCreatedDate();
        sessionManager.getUserSessions().put(userId, userSession);

        assertSame(userSession, sessionManager.createUserSession(userId));
        assertFalse(sessionManager.getUserSessions().get(userId).getCreatedDate().before(oldCreatedDate));
    }

    @Test
    public void testCreateUserSessionNotLoggedInUser() {
        int userId = getRandomUserId();
        UserSession userSession = sessionManager.createUserSession(userId);

        assertNotNull(userSession);
        assertNotNull(userSession.getSessionKey());
        assertNotNull(userSession.getCreatedDate());
        assertNotNull(userSession.getUserId());
        assertTrue(sessionManager.getUserSessions().containsValue(userSession));
    }

    @Test
    public void testGetUserSessionLoggedInUser() {
        // Login a user
        int userId = getRandomUserId();
        String sessionKey = getRandomSessionKey();
        UserSession userSession = new UserSession(userId, sessionKey);
        sessionManager.getUserSessions().put(userId, userSession);

        assertSame(userSession, sessionManager.getUserSession(sessionKey));
    }

    @Test
    public void testGetUserSessionNotLoggedInUser() {
        String sessionKey = getRandomSessionKey();
        assertNull(sessionManager.getUserSession(sessionKey));
    }

    @Test
    public void testRemoveUserSessionsValidTimeout() {
        // Sets timeout to 10 minutes
        int TIMEOUT = 10 * 60 * 1000;

        // Login a user
        int userId = getRandomUserId();
        String sessionKey = getRandomSessionKey();
        UserSession userSession = new UserSession(userId, sessionKey);

        // Set created date 15 minutes in the past
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, -(15 * 60 * 1000));
        userSession.setCreatedDate(cal.getTime());

        sessionManager.getUserSessions().put(userId, userSession);

        sessionManager.removeUserSessions(TIMEOUT);

        assertFalse(sessionManager.getUserSessions().containsKey(userId));
    }

    @Test
    public void testRemoveUserSessionsNotValidTimeout() {
        // Login a user
        int TIMEOUT = 10 * 60 * 1000;
        int userId = getRandomUserId();
        String sessionKey = getRandomSessionKey();
        UserSession userSession = new UserSession(userId, sessionKey);
        sessionManager.getUserSessions().put(userId, userSession);

        sessionManager.removeUserSessions(TIMEOUT);

        assertTrue(sessionManager.getUserSessions().containsKey(userId));
    }

    private int getRandomUserId() {
        return Math.abs(new BigInteger(130, random).intValue());
    }

    private String getRandomSessionKey() {
        return new BigInteger(130, random).toString(32);
    }
}
