package player;

import game.Game;

public class AI extends Player {
    private static final String[] AI_NAMES = {"Jean", "Antoine", "Fabrice", "Patrick", "Clara", "June", "Louis", "Silvain"};

    public final Strategy strategy;

    public AI() {
        super(randomAIName());
        System.out.print("AI " + this.getName() + " is ");
        this.strategy = switch (Game.randomInInterval(0, 1)) {
            case 0 -> {
                System.out.println("agressive !");
                yield new Agressive();
            }
            default -> {
                System.out.println("defensive !");
                yield new Defensive();
            }
        };
    }

    /**
     * Get a random not already assigned name
     * @return New name
     */
    private static String randomAIName() {
        String name;
        boolean nameAssigned = false;
        do {
            name = AI_NAMES[Game.randomInInterval(0, AI_NAMES.length - 1)];
            for (Player player : Game.getGame().players) {
                nameAssigned = name.equals(player.getName());
                if (nameAssigned) break;
            }
        } while (nameAssigned);
        return name;
    }

    /**
     * Use the AI strategy to select action
     */
    public void play() {
        System.out.println("Currently using " + this.getName() + "'s player.Strategy !"); //Used for debug
        this.strategy.use(this);
    }

    /**
     * Use the AI strategy to select identity
     */
    public void selectIdentity() {
        this.strategy.selectIdentity(this);
    }

}