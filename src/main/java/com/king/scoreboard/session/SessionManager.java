package com.king.scoreboard.session;

import com.king.scoreboard.properties.ConfigProperties;
import com.king.scoreboard.service.UserLogoutTimerTask;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SessionManager {

    private static final Logger LOGGER = Logger.getLogger("confLogger");

    private static volatile SessionManager instance = null;
    private final static Object lock = new Object();

    private final Map<Integer, UserSession> userSessions = new ConcurrentHashMap<>();
    private final UserLogoutTimerTask userLogoutTimerTask;
    private final Timer timer;

    /**
     * It helps to random secure strings
     */
    private static final SecureRandom random = new SecureRandom();

    private SessionManager() {
        // Schedules and starts the logout daemon task
        userLogoutTimerTask = new UserLogoutTimerTask(Long.parseLong(ConfigProperties.LOGOUT_TIMEOUT.getValue()));
        this.timer = new Timer(true);
        timer.scheduleAtFixedRate(userLogoutTimerTask, Long.parseLong(ConfigProperties.LOGOUT_TIMEOUT_PERIOD_DELAY.getValue()), Long.parseLong(ConfigProperties.LOGOUT_TIMEOUT_PERIOD_CHECK.getValue()));
    }

    /**
     * Creates or reuses the manager's instance.
     * Ensures that only a Singleton instance is used.
     *
     * @return the manager's instance.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null)
                    instance = new SessionManager();
            }
        }
        return instance;
    }

    /**
     * Initializes the manager
     */
    public static void init() {
        getInstance();
    }

    public Timer getTimer() {
        return timer;
    }

    /**
     * This works by choosing 130 bits from a cryptographically secure random bit generator, and encoding them in base-32.
     * 128 bits is considered to be cryptographically strong, but each digit in a base 32 number can encode 5 bits,
     * so 128 is rounded up to the next multiple of 5. This encoding is compact and efficient, with 5 random bits per character.
     *
     * @return a new fairly unique session key
     */
    private static String createSessionKey() {
        return new BigInteger(130, random).toString(32);
    }

    /**
     * Returns a new or updated user session from the given userId.
     * The session either is created if it does not exist or its date is updated to the current one.
     * <p>
     * The map operations are thread-safe.
     *
     * @param userId
     * @return a new or updated user session from the given userId
     */
    public UserSession createUserSession(int userId) {
        UserSession userSession = userSessions.get(userId);
        if (userSession != null) {
            userSession.updateCreatedDate();
            userSessions.replace(userId, userSession);
        } else {
            userSession = new UserSession(userId, createSessionKey());
            userSessions.putIfAbsent(userId, userSession);
        }
        return userSession;
    }

    /**
     * Returns a user session from the given sessionKey.
     * Returns null, if the user is not logged in.
     *
     * @param sessionKey to retrieve a user session
     * @return a user session from the given sessionKey
     */
    public UserSession getUserSession(String sessionKey) {
        Iterator<Map.Entry<Integer, UserSession>> iter = userSessions.entrySet().iterator();
        while (iter.hasNext()) {
            UserSession userSession = iter.next().getValue();
            if (userSession.getSessionKey().equals(sessionKey)) {
                return userSession;
            }
        }
        return null;
    }

    /**
     * Removes the sessions that are expired based on the given timeout
     *
     * @param logoutTimeout is the criterion for removing a user session
     */
    public void removeUserSessions(long logoutTimeout) {
        final Date now = new Date();
        for (UserSession userSession : userSessions.values()) {
            if (now.getTime() - userSession.getCreatedDate().getTime() > logoutTimeout) {
                userSession = userSessions.remove(userSession.getUserId());
                LOGGER.info("UserLogoutTimerTask: Removing userSession [" + userSession + "]");
            }
        }
    }

    /**
     * Returns the user sessions map
     *
     * @return the user sessions map
     */
    public Map<Integer, UserSession> getUserSessions() {
        return userSessions;
    }
}
