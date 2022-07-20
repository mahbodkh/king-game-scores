package com.king.scoreboard;

import com.king.scoreboard.userscore.UserScore;
import com.king.scoreboard.userscore.ScoreComparator;
import com.king.scoreboard.userscore.UserScoreManager;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.Assert.*;


public class UserScoreManagerTest {

    private UserScoreManager userScoreManager;
    private static SecureRandom random;

    @Before
    public void setUp() {
        userScoreManager = UserScoreManager.getInstance();
        random = new SecureRandom();
    }

    @Test
    public void testSingletonInstance() {
        assertSame(UserScoreManager.getInstance(), UserScoreManager.getInstance());
    }

    @Test
    public void testPutUserScoreLevelExisted() {
        // Add a level with a user score before the call
        Integer levelId = getRandomId();
        UserScore oldUserScore = new UserScore(getRandomUserId(), getRandomId());
        ConcurrentSkipListSet<UserScore> oldConcurrentSkipListSet = new ConcurrentSkipListSet<>(new ScoreComparator());
        oldConcurrentSkipListSet.add(oldUserScore);
        userScoreManager.getUserScores().put(levelId, oldConcurrentSkipListSet);

        // Call with a new user score
        UserScore newUserScore = new UserScore(getRandomUserId(), getRandomId());
        userScoreManager.putUserScore(levelId, newUserScore);

        // Asserts that given levelId is contained in the set
        assertTrue(userScoreManager.getUserScores().containsKey(levelId));

        // Asserts that the given and returned set objects are the same
        ConcurrentSkipListSet<UserScore> newConcurrentSkipListSet = userScoreManager.getUserScores().get(levelId);
        assertEquals(newConcurrentSkipListSet, oldConcurrentSkipListSet);

        // Assert that all the given user scores still exist in the set
        assertTrue(newConcurrentSkipListSet.contains(oldUserScore));
        assertTrue(newConcurrentSkipListSet.contains(newUserScore));

        userScoreManager.getUserScores().clear();
    }

    @Test
    public void testPutUserScoreLevelNotExisted() {
        Integer levelId = getRandomId();
        UserScore userScore = new UserScore(getRandomUserId(), getRandomId());
        userScoreManager.putUserScore(levelId, userScore);
        assertTrue(userScoreManager.getUserScores().containsKey(levelId));
        assertEquals(userScoreManager.getUserScores().size(), 1);

        userScoreManager.getUserScores().clear();
    }

    @Test
    public void testPutUserScoreAddUserScoresToOneExistedLevelAndManyOtherNonExistedLevelsMultipleTreads() {
        int NUMBER_OF_EXISTED_LEVELS = 1;
        int NUMBER_OF_EXISTED_SCORES = 5;
        int NUMBER_OF_NEW_SCORES_IN_EXISTED_LEVEL = 5;
        int NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL = 5;

        Integer existedLevelId = getRandomId();
        UserScore[] existedUserScores = new UserScore[NUMBER_OF_EXISTED_SCORES];

        // temp test map
        Map<Integer, UserScore> mapValues = new HashMap<>();

        // add a level with 5 scores
        ConcurrentSkipListSet<UserScore> existedConcurrentSkipListSet = new ConcurrentSkipListSet<UserScore>(new ScoreComparator());
        for (int i = 0; i < NUMBER_OF_EXISTED_SCORES; i++) {
            existedUserScores[i] = new UserScore(getRandomUserId(), getRandomId());
            // Stores values for later testing
            mapValues.put(existedLevelId, existedUserScores[i]);
            // Stores values in the existed set
            existedConcurrentSkipListSet.add(existedUserScores[i]);
        }
        // Put the set in the map
        userScoreManager.getUserScores().put(existedLevelId, existedConcurrentSkipListSet);

        // threads run in parallel and do the call
        runThreads(mapValues, NUMBER_OF_NEW_SCORES_IN_EXISTED_LEVEL, NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL, existedLevelId);

        // Assert that all levels are contained in the map associated with the correct user scores
        for (Integer levelId : mapValues.keySet()) {
            assertTrue("All keys are contained in the map", userScoreManager.getUserScores().containsKey(levelId));
            assertTrue("All user-scores are contained in the map", userScoreManager.getUserScores().get(levelId).contains(mapValues.get(levelId)));
            if (Objects.equals(levelId, existedLevelId)) {
                assertEquals("Existent level have the correct size", NUMBER_OF_EXISTED_SCORES + NUMBER_OF_NEW_SCORES_IN_EXISTED_LEVEL, userScoreManager.getUserScores().get(levelId).size());
            } else {
                assertEquals("Non-existent levels have the correct size", 1, userScoreManager.getUserScores().get(levelId).size());
            }
        }

        // Assert that the map has the correct size
        assertEquals("Map has the correct size", NUMBER_OF_EXISTED_LEVELS + NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL, userScoreManager.getUserScores().size());

        userScoreManager.getUserScores().clear();
    }

    @Test
    public void testPutUserScoreLevelsNotExistedMultipleTreads() {
        int NUMBER_OF_THREADS = 10;
        Map<Integer, UserScore> mapValues = new HashMap<>();

        // Initiates and runs threads in parallel
        UserScoreThread[] threads = runThreads(mapValues, NUMBER_OF_THREADS, 0, null);

        // Assert that all levels are contained in the map associated with the correct user scores
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            assertTrue("Map contains key", userScoreManager.getUserScores().containsKey(threads[i].getLevelId()));
            assertTrue("Key is associated with the correct user score", userScoreManager.getUserScores().get(threads[i].getLevelId()).contains(mapValues.get(threads[i].getLevelId())));
        }

        // Assert that the map has the correct size
        assertEquals("Map has correct size", NUMBER_OF_THREADS, userScoreManager.getUserScores().size());

        userScoreManager.getUserScores().clear();
    }

    @Test
    public void testGetHighScoreListLevelHasScoresMoreThanLimit() {
        //do assertion for the limit and response
        Integer levelId = getRandomId();
        int SCORES_LIMIT = 10;
        int ADDED_SCORES = 15;

        // add more user scores than the limit
        Map<String, Integer> mapValues = new HashMap<>();
        addUserScores(mapValues, levelId, ADDED_SCORES, false);

        String highScoreList = userScoreManager.getHighScoreList(levelId, SCORES_LIMIT);

        assertEquals("HishScoreList string size", SCORES_LIMIT, highScoreList.split(",").length);

        String[] subStrings = highScoreList.split(",");
        for (int i = 0; i < subStrings.length - 1; i++) {
            assertTrue("Pair score ordering ascending", Integer.parseInt(subStrings[i].split("=")[1]) > Integer.parseInt(subStrings[i + 1].split("=")[1]));
        }

        userScoreManager.getUserScores().clear();
    }

    @Test
    public void testGetHighScoreListLevelHasScoresWithSameUserId() {
        //do assertion for the limit and response
        Integer levelId = getRandomId();
        int SCORES_LIMIT = 10;
        int ADDED_SCORES = 15;

        // add more user scores than the limit
        Map<String, Integer> mapValues = new HashMap<>();

        addUserScores(mapValues, levelId, ADDED_SCORES, true);
        addUserScores(mapValues, levelId, ADDED_SCORES, true);

        String highScoreList = userScoreManager.getHighScoreList(levelId, SCORES_LIMIT);

        assertEquals("HishScoreList string size", 2, highScoreList.split(",").length);

        userScoreManager.getUserScores().clear();
    }

    @Test
    public void testGetHighScoreListLevelHasScoresLessThanLimit() {
        Integer levelId = getRandomId();
        int SCORES_LIMIT = 10;
        int ADDED_SCORES = 5;

        // add more user scores than the limit
        Map<String, Integer> mapValues = new HashMap<>();
        addUserScores(mapValues, levelId, ADDED_SCORES, false);

        String highScoreList = userScoreManager.getHighScoreList(levelId, SCORES_LIMIT);

        for (String userId : mapValues.keySet()) {
            Integer score = mapValues.get(userId);
            assertTrue("Each userId-score exists in the string", highScoreList.contains(userId + "=" + score));
        }

        assertEquals("HighScoreList string size", ADDED_SCORES, highScoreList.split(",").length);

        String[] subStrings = highScoreList.split(",");
        for (int i = 0; i < subStrings.length - 1; i++) {
            assertTrue("Pair score ordering ascending", Integer.parseInt(subStrings[i].split("=")[1]) > Integer.parseInt(subStrings[i + 1].split("=")[1]));
        }

        userScoreManager.getUserScores().clear();
    }

    @Test
    public void testGetHighScoreListLevelHasNoScores() {
        Integer levelId = getRandomId();
        int SCORES_LIMIT = 15;

        assertEquals("", userScoreManager.getHighScoreList(levelId, SCORES_LIMIT));
    }

    /**
     * Adds a number of user scores to a specific levelId and stores the values in a temporary given map
     *
     * @param mapValues
     * @param levelId
     * @param ADDED_SCORES
     */
    public void addUserScores(Map<String, Integer> mapValues, Integer levelId, final int ADDED_SCORES, boolean sameUserId) {
        ConcurrentSkipListSet<UserScore> concurrentSkipListSetLevel = new ConcurrentSkipListSet<>(new ScoreComparator());
        String userId = getRandomUserId();
        for (int i = 0; i < ADDED_SCORES; i++) {
            userId = sameUserId ? userId : getRandomUserId();
            UserScore userScore = new UserScore(userId, getRandomId());
            mapValues.put(userScore.getUserId(), userScore.getScore());
            concurrentSkipListSetLevel.add(userScore);
        }
        if (userScoreManager.getUserScores().get(levelId) != null) {
            userScoreManager.getUserScores().get(levelId).addAll(concurrentSkipListSetLevel);
        } else {
            userScoreManager.getUserScores().put(levelId, concurrentSkipListSetLevel);
        }
    }

    /**
     * Runs and returns the treads.
     * It starts two types of groups of  threads, one with an existing score level and one without.
     *
     * @param mapValues
     * @param NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL
     * @param NUMBER_OF_NEW_SCORES_IN_EXISTED_LEVEL
     * @param existedLevelId
     * @return the run threads
     */
    private UserScoreThread[] runThreads(Map mapValues, final int NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL, final int NUMBER_OF_NEW_SCORES_IN_EXISTED_LEVEL, Integer existedLevelId) {
        UserScoreThread[] threads = new UserScoreThread[NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL + NUMBER_OF_NEW_SCORES_IN_EXISTED_LEVEL];

        startThreads(mapValues, threads, NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL, null);
        startThreads(mapValues, threads, NUMBER_OF_NEW_SCORES_IN_EXISTED_LEVEL, existedLevelId);

        try {
            for (int i = 0; i < NUMBER_OF_NEW_SCORES_IN_NON_EXISTED_LEVEL; i++) {
                threads[i].join();
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getCause());
        }
        return threads;
    }

    /**
     * Starts the threads.
     * If the existedLevelId is null, it generates random level ids
     *
     * @param mapValues
     * @param threads
     * @param NUMBER_OF_SCORES
     * @param existedLevelId
     */
    private void startThreads(Map mapValues, UserScoreThread[] threads, final int NUMBER_OF_SCORES, Integer existedLevelId) {
        for (int i = 0; i < NUMBER_OF_SCORES; i++) {
            Integer levelId = existedLevelId != null ? existedLevelId : getRandomId();
            UserScore userScore = new UserScore(getRandomUserId(), getRandomId());
            // Store values for later testing
            mapValues.put(levelId, userScore);

            // Create thread
            threads[i] = new UserScoreThread(userScoreManager, levelId, userScore);
            threads[i].start();
        }
    }

    private int getRandomId() {
        return Math.abs(new BigInteger(130, random).intValue());
    }

    private String getRandomUserId() {
        return String.valueOf(Math.abs(new BigInteger(130, random).intValue()));
    }
}

/**
 * Represents a user score thread for test-stretch the user-scores map
 */
class UserScoreThread extends Thread {

    private final Integer levelId;
    private final UserScore userScore;
    private final UserScoreManager userScoreManager;

    public UserScoreThread(UserScoreManager userScoreManager, int levelId, UserScore userScore) {
        this.userScoreManager = userScoreManager;
        this.levelId = levelId;
        this.userScore = userScore;
    }

    @Override
    public void run() {
        userScoreManager.putUserScore(levelId, userScore);
    }

    public Integer getLevelId() {
        return levelId;
    }
}
