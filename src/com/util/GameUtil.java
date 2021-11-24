package com.util;

import com.model.player.Player;

import java.util.List;
import java.util.Random;

/**
 * The type Game util.
 */
public final class GameUtil {
    /**
     * Instantiates a new Game util.
     */
    private GameUtil() {
    }

    /**
     * Get random integer between 0 and the max.
     * This function is a utility function used to get a random integer between 0 and the max value (included).
     *
     * @param max the maximum value to be returned (included)
     * @return random integer in the interval [0;max]
     */
    public static int randomInInterval(int max) {
        return max == 0 ? 0 : new Random().nextInt(max + 1);
    }

    /**
     * Get a random not already assigned name.
     *
     * @param players the players
     * @return new name
     */
    public static String randomAIName(List<Player> players) {
        String[] NAMES = {"Jean", "Antoine", "Fabrice", "Patrick", "Clara", "June", "Louis", "Sylvain"};

        String name;
        boolean nameAssigned = false;
        do {
            name = NAMES[randomInInterval(NAMES.length - 1)];
            for (Player player : players) {
                nameAssigned = player.getName().equals(name);
                if (nameAssigned) break;
            }
        } while (nameAssigned);
        return name;
    }
}