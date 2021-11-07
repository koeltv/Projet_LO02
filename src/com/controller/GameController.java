package com.controller;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.card.effect.*;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;
import com.view.CommandLineView;
import com.view.GraphicalInterfaceView;
import com.view.Views;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The type Game controller.
 */
public class GameController {
    /**
     * The Players.
     */
    public List<Player> players;

    /**
     * The Deck.
     */
    public final List<RumourCard> deck;

    private final ActiveView view;

    /**
     * Action choice at the end of a game.
     */
    enum GameAction {
        /**
         * Restart a new game with existing players.
         */
        RESTART_GAME,
        /**
         * Restart a new game with new players.
         */
        RESET_GAME,
        /**
         * End the program.
         */
        STOP
    }

    /**
     * Instantiates a new Game controller.
     *
     * @param view the view
     */
    public GameController(ActiveView view) {
        this.deck = new ArrayList<>();
        this.view = view;
    }

    /**
     * Get random integer in a given interval.
     * This function is a utility function used to get a random integer between 2 limits (included).
     *
     * @param min the minimum value to be returned (included)
     * @param max the maximum value to be returned (included)
     * @return random integer in the interval
     */
    public static int randomInInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt((max + 1) - min) + min;
    }

    /**
     * Get a random not already assigned name.
     *
     * @return new name
     */
    public String randomAIName() {
        String[] NAMES = {"Jean", "Antoine", "Fabrice", "Patrick", "Clara", "June", "Louis", "Silvain"};

        String name;
        boolean nameAssigned = false;
        do {
            name = NAMES[GameController.randomInInterval(0, NAMES.length - 1)];
            for (Player player : players) {
                nameAssigned = player.getName().equals(name);
                if (nameAssigned) break;
            }
        } while (nameAssigned);
        return name;
    }

    private void askForPlayerRepartition() {
        int[] values;
        do {
            values = view.promptForRepartition();
        } while (values.length < 2 || values[0] + values[1] < 3 || values[0] + values[1] > 6);

        for (int i = 0; i < values[0]; i++) {
            addPlayer(view.promptForPlayerName(i));
        }

        for (int i = 0; i < values[1]; i++) {
            players.add(new AI(randomAIName()));
        }
    }

    /**
     * Add player.
     *
     * @param playerName the player name
     */
    private void addPlayer(String playerName) {
        boolean nameAlreadyAssigned = false;
        for (Player player : players) {
            nameAlreadyAssigned = player.getName().equals(playerName);
            if (nameAlreadyAssigned) break;
        }
        if (!nameAlreadyAssigned) {
            players.add(new Player(playerName));
        }
    }

    private void setupGame() {
        for (CardName cardName : CardName.values()) {
            List<Effect> witchEffects = new ArrayList<>();
            List<Effect> huntEffect = new ArrayList<>();

            //Witch? effects
            switch (cardName) {
                case THE_INQUISITION -> {
                    witchEffects.add(new DiscardFromHandEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case POINTED_HAT -> {
                    witchEffects.add(new TakeRevealedCardEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case HOOKED_NOSE -> {
                    witchEffects.add(new TakeFromAccuserHandEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case DUCKING_STOOL -> witchEffects.add(new ChooseNextEffect());
                case CAULDRON -> {
                    witchEffects.add(new AccuserDiscardRandomEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case EVIL_EYE -> {
                    witchEffects.add(new ChooseNextEffect());
                    witchEffects.add(new NextMustAccuseOtherEffect());
                }
                default -> witchEffects.add(new TakeNextTurnEffect());
            }
            //Hunt! Effects
            switch (cardName) {
                case ANGRY_MOB -> huntEffect.add(new RevealAnotherIdentityEffect());
                case THE_INQUISITION -> {
                    huntEffect.add(new ChooseNextEffect());
                    huntEffect.add(new SecretlyReadIdentityEffect());
                }
                case POINTED_HAT -> {
                    huntEffect.add(new TakeRevealedCardEffect());
                    huntEffect.add(new ChooseNextEffect());
                }
                case HOOKED_NOSE -> {
                    huntEffect.add(new ChooseNextEffect());
                    huntEffect.add(new TakeRandomCardFromNextEffect());
                }
                case DUCKING_STOOL -> huntEffect.add(new RevealOrDiscardEffect());
                case CAULDRON, TOAD -> huntEffect.add(new RevealOwnIdentityEffect());
                case EVIL_EYE -> {
                    huntEffect.add(new ChooseNextEffect());
                    huntEffect.add(new NextMustAccuseOtherEffect());
                }
                case BLACK_CAT -> huntEffect.add(new DiscardedToHandEffect());
                case PET_NEWT -> huntEffect.add(new TakeRevealedFromOtherEffect());
                default -> huntEffect.add(new ChooseNextEffect());
            }

            deck.add(new RumourCard(cardName, witchEffects, huntEffect));
        }
    }

    /**
     * Exit or start a new game.
     *
     * @return the choice, false to continue, true to exit
     */
    public GameAction nextAction() {
        return switch (view.promptForNewGame()) {
            case "q" -> GameAction.STOP;
            case "r" -> GameAction.RESET_GAME;
            default -> GameAction.RESTART_GAME;
        };
    }

    private boolean verifyScores() {
        for (Player player : players)
            if (player.getScore() >= 5) return true;
        return false;
    }

    private void wrapUpGame() {
        List<Player> winners = new ArrayList<>();

        players.forEach(player -> {
            if (player.getScore() >= 5) winners.add(player);
            player.resetScore();
        });

        if (winners.size() > 1) {
            settleTie(winners);
        } else if (winners.size() == 1) {
            view.showGameWinner(winners.get(0).getName(), RoundController.getNumberOfRound());
        }

        RoundController.reset();
    }

    private void settleTie(List<Player> winners) {
        Player winner = winners.get(randomInInterval(0, winners.size() - 1));
        view.showGameWinner(winner.getName(), RoundController.getNumberOfRound());
    }

    /**
     * Run the game.
     */
    public void run() {
        GameAction endProgram;
        setupGame();
        do {
            players = new ArrayList<>();
            askForPlayerRepartition();
            do {
                do {
                    RoundController roundController = new RoundController(this, view);
                    roundController.run();
                } while (!verifyScores());
                wrapUpGame();
                endProgram = nextAction();
            } while (endProgram == GameAction.RESTART_GAME);
        } while (endProgram == GameAction.RESET_GAME);
        //Force exit, not needed with CommandLineView but needed for GraphicalView types
        System.exit(0);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments, currently unused
     */
    public static void main(String[] args) {
        Views views = new Views(new GraphicalInterfaceView());
        views.addView(new CommandLineView());

        GameController gameController = new GameController(views);
        gameController.run();
    }
}
