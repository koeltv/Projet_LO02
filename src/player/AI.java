package player;

import game.Game;

public class AI extends Player {
    private static final String[] AI_NAMES = {"Jean", "Antoine", "Fabrice", "Patrick", "Clara", "June", "Louis", "Silvain"};

    public final Strategy strategy;

    public AI() {
        super(randomAIName());
        this.strategy = switch (Game.randomInInterval(0, 1)) {
            case 0 -> new Agressive();
            default -> new Defensive();
        };
    }

    private static String randomAIName() {
        return AI_NAMES[Game.randomInInterval(0, AI_NAMES.length)];
    }

    public void play() {
        System.out.println("Currently using " + this.getName() + "'s player.Strategy !"); //Used for debug
        this.strategy.use();
    }

}
