package com.controller;

import com.model.card.Deck;
import com.model.game.Round;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;
import com.view.CommandLineView;
import com.view.Views;
import com.view.graphic.dynamic.Graphical2DView;

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
    List<PlayerController> players;

    /**
     * The Deck.
     */
    final Deck deck;

    /**
     * The View.
     */
    private final ActiveView view;

    /**
     * Instantiates a new Game controller.
     *
     * @param view the view
     */
    public GameController(ActiveView view) {
        this.players = new ArrayList<>();
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
        players.clear();
        int[] values;
        do {
            values = view.promptForRepartition();
        } while (!repartitionAllowed(values[0], values[1]));

        for (int i = 1; i <= values[0]; i++) addPlayer(i);
        for (int i = 0; i < values[1]; i++) {
            players.add(new AIController(
                    randomAIName(players.stream()
                            .map(player -> player.getPlayer().getName())
                            .collect(Collectors.toList()))
                    ,view
                    ));
        }
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
            for (PlayerController player : players) {
                nameAlreadyAssigned = player.getName().equals(playerName);
                if (nameAlreadyAssigned) break;
            }
        } while (nameAlreadyAssigned);
        players.add(new PlayerController(playerName, view));
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
        return players.stream().anyMatch(player -> player.getScore() >= 5);
    }

    /**
     * Wrap up game.
     */
    private void wrapUpGame() {
        List<Player> winners = players.stream().map(PlayerController::getPlayer).filter(player -> player.getScore() >= 5).collect(Collectors.toList());

        view.showGameWinner(settleTie(winners).getName(), RoundController.getNumberOfRound());

        players.forEach(playerController -> playerController.getPlayer().resetScore());
        Round.reset();
    }

    /**
     * Settle tie.
     *
     * @param winners the winners
     * @return the winning player
     */
    private Player settleTie(List<Player> winners) { //TODO Find better alternative
        if (winners.size() > 1) {
            return winners.get(randomInInterval(winners.size() - 1));
        } else {
            return winners.get(0);
        }
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
        views.addView(new Graphical2DView());

        GameController gameController = new GameController(views);
        gameController.run();
    }
}
