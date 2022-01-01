package com.util;

import java.util.List;
import java.util.Random;

/**
 * The type Game util.
 * 
 * Gives all the other methods related with the game, that are used sometimes.
 */
public final class GameUtil {
    
	/**
     * Instantiates a new Game util.
     */
    private GameUtil() {
    }

    /**
     * The possible names to choose from.
     * This function is used to give names to AI in the game.
     */
    private static final String[] NAMES = {"Jean", "Antoine", "Fabrice", "Patrick", "Clara", "June", "Louis", "Sylvain"};

    /**
     * Get random integer between 0 and the max.
     * This function is a useful function used to get a random integer between 0 and the max value (included).
     *
     * @param max the maximum value to be returned (included)
     * @return random integer in the interval [0;max]
     */
    public static int randomInInterval(int max) {
        return max == 0 ? 0 : new Random().nextInt(max + 1);
    }

    /**
     * Get a random not already assigned name.
     * This function is used to give names to AI in the game.
     *
     * @param playerNames the players name
     * @return new name
     */
    public static String randomAIName(List<String> playerNames) {
        String name;
        do {
            name = NAMES[randomInInterval(NAMES.length - 1)];
        } while (playerNames.contains(name));
        return name;
    }
}
