
public class AI extends Player {
    private final int difficulty;

    private static final String[] AI_NAMES = {"Jean", "Antoine", "Fabrice", "Patrick"};

    AI(final int difficulty) {
        super(randomAIName());
        this.difficulty = difficulty;
    }

    private static String randomAIName() {
        return AI_NAMES[Game.randomInInterval(0, AI_NAMES.length)];
    }

    public int getDifficulty() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.difficulty;
    }

}
