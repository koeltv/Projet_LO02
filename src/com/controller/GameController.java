package com.controller;

import com.model.card.Deck;
import com.model.game.Game;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;
import com.view.InitialViewChoice;

import java.util.List;
import java.util.stream.Collectors;

import static com.util.GameUtil.randomAIName;

/**
 * The type Game controller.
 * 
 * Game controller is the class that contains the function main() in order to execute the program.
 */
public class GameController {
    
	/**
     * The Game.
     * 
     * @see com.model.game.Game
     */
    private final Game game;

    /**
     * The View.
     * 
     * @see com.view.ActiveView
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

    /**
     * Gets deck.
     *
     * @return the deck
     * @see com.model.game.Game
     */
    public Deck getDeck() {
        return game.getDeck();
    }
    
    /**
     * Gets players.
     *
     * @return the players
     * @see com.model.game.Game
     */
    public List<Player> getPlayers() {
        return game.getPlayers();
    }

    /**
     * Ask for player repartition.
     * 
     * @see com.model.game.Game
     * @see com.model.player.Player
     * @see com.view.ActiveView
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
     * @see com.model.game.Game
     * @see com.model.player.Player
     * @see com.view.ActiveView
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
     * The user can press "q" to exit the game, "r" to reset the game and "" to restart.
     *
     * @return the choice, false to continue, true to exit
     * @see com.controller.GameAction
     * @see com.view.ActiveView
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
     * 
     * @see com.controller.RoundController
     * @see com.model.game.Game
     * @see com.model.player.Player
     * @see com.view.View
     */
    private void wrapUpGame() {
        List<Player> winners = game.getPlayers().stream().filter(player -> player.getScore() >= 5).collect(Collectors.toList());

        view.showGameWinner(game.settleTie(winners).getName(), RoundController.getNumberOfRound());
    }

    /**
     * Run the game.
     * 
     * @see com.controller.GameAction
     * @see com.controller.RoundController
     * @see com.model.game.Game
     */
    public void run() {
        GameAction endProgram;
        do {
            askForPlayerRepartition();
            do {
                game.resetScores();
                RoundController.reset();
                do {
                    RoundController.reset(this, view);
                    RoundController.getInstance().run();
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
     * @see com.view.CommandLineView
     * @see com.view.Views
     * @see com.view.graphic.dynamic.Graphical2DView
     */
    public static void main(String[] args) {
        ActiveView activeView = InitialViewChoice.run();
        GameController gameController = new GameController(activeView);
        gameController.run();
    }
}
