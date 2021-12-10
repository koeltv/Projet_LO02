package com.controller;

import com.model.card.Deck;
import com.model.game.Game;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;
import com.view.CommandLineView;
import com.view.Views;
import com.view.graphic.dynamic.Graphical2DView;

import java.util.List;
import java.util.stream.Collectors;

import static com.util.GameUtil.randomAIName;

/**
 * The type Game controller.
 */
public class GameController {
    /**
     * The Game.
     */
    private final Game game;

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
        this.game = new Game();
        this.view = view;
    }

    public Deck getDeck() {
        return game.getDeck();
    }

    public List<Player> getPlayers() {
        return game.getPlayers();
    }

    /**
     * Ask for player repartition.
     */
    private void askForPlayerRepartition() {
        game.clearPlayers();
        int[] values;
        do {
            values = view.promptForRepartition();
        } while (!game.repartitionAllowed(values[0], values[1]));

        for (int i = 1; i <= values[0]; i++) addPlayer(i);
        for (int i = 0; i < values[1]; i++) {
            game.getPlayers().add(new AI(randomAIName(game.getPlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList())
            )));
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
            for (Player player : game.getPlayers()) {
                nameAlreadyAssigned = player.getName().equals(playerName);
                if (nameAlreadyAssigned) break;
            }
        } while (nameAlreadyAssigned);
        game.addPlayer(new Player(playerName));
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
     * Wrap up game.
     */
    private void wrapUpGame() {
        List<Player> winners = game.getPlayers().stream().filter(player -> player.getScore() >= 5).collect(Collectors.toList());

        view.showGameWinner(game.settleTie(winners).getName(), RoundController.getNumberOfRound());
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
                    game.resetScores();
                    RoundController.reset();
                    new RoundController(this, view).run();
                } while (!game.verifyScores());
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
        Views views = new Views(new Graphical2DView());
        views.addView(new CommandLineView());

        GameController gameController = new GameController(views);
        gameController.run();
    }
}
