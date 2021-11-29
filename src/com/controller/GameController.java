package com.controller;

import com.model.card.Deck;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;
import com.view.CommandLineView;
import com.view.Views;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.util.GameUtil.randomAIName;
import static com.util.GameUtil.randomInInterval;

/**
 * The type Game controller.
 */
public class GameController {
    /**
     * The Players.
     */
    List<Player> players;

    /**
     * The Deck.
     */
    final Deck deck;

    /**
     * The View.
     */
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
        this.deck = new Deck();
        this.view = view;
    }

    /**
     * Check if the given repartition is allowed.
     *
     * @param nbOfPlayers the number of players
     * @param nbOfAIs     the number of AIs
     * @return true if the repartition is valid, false otherwise
     */
    private boolean repartitionAllowed(int nbOfPlayers, int nbOfAIs) {
        if (nbOfPlayers + nbOfAIs >= 3 && nbOfPlayers + nbOfAIs <= 6) {
            return nbOfPlayers >= 0 && nbOfPlayers <= 6 && nbOfAIs >= 0 && nbOfAIs <= 6;
        }
        return false;
    }

    /**
     * Ask for player repartition.
     */
    private void askForPlayerRepartition() {
        players = new ArrayList<>();
        int[] values;
        do {
            values = view.promptForRepartition();
        } while (!repartitionAllowed(values[0], values[1]));

        for (int i = 1; i <= values[0]; i++) addPlayer(i);
        for (int i = 0; i < values[1]; i++)
            players.add(new AI(randomAIName(players.stream().map(Player::getName).collect(Collectors.toList()))));
    }

    /**
     * Add player.
     *
     * @param id the player id
     */
    private void addPlayer(int id) {
        String playerName;
        boolean nameAlreadyAssigned;
        do {
            playerName = view.promptForPlayerName(id);
            nameAlreadyAssigned = false;
            for (Player player : players) {
                nameAlreadyAssigned = player.getName().equals(playerName);
                if (nameAlreadyAssigned) break;
            }
        } while (nameAlreadyAssigned);
        players.add(new Player(playerName));
    }

    /**
     * Exit or start a new game.
     *
     * @return the choice, false to continue, true to exit
     */
    private GameAction nextAction() {
        return switch (view.promptForNewGame()) {
            case "q" -> GameAction.STOP;
            case "r" -> GameAction.RESET_GAME;
            default -> GameAction.RESTART_GAME;
        };
    }

    /**
     * Verify scores.
     *
     * @return true if at least 1 player has 5 points or more, and false otherwise
     */
    private boolean verifyScores() {
        for (Player player : players)
            if (player.getScore() >= 5) return true;
        return false;
    }

    /**
     * Wrap up game.
     */
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

    /**
     * Settle tie.
     *
     * @param winners the winners
     */
    private void settleTie(List<Player> winners) { //TODO Find better alternative
        Player winner = winners.get(randomInInterval(winners.size() - 1));
        view.showGameWinner(winner.getName(), RoundController.getNumberOfRound());
    }

    /**
     * Run the game.
     */
    public void run() {
        GameAction endProgram;
        do {
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
        Views views = new Views(new CommandLineView());
        //        views.addView(new Graphical2DView());

        GameController gameController = new GameController(views);
        gameController.run();
    }
}
