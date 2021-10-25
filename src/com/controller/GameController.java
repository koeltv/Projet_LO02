package com.controller;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.card.effect.*;
import com.model.player.AI;
import com.model.player.Player;
import com.view.CommandLineView;
import com.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    enum GameState {
        ADDING_PLAYERS, PLAYERS_ADDED, GAME_INITIATED, GAME_COMPLETED, END_OF_SESSION
    }

    public final List<Player> players;

    public final List<RumourCard> deck;

    private final View view;

    GameState gameState;

    public GameController(View view) {
        super();
        this.players = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.view = view;
        this.gameState = GameState.ADDING_PLAYERS;
        view.setController(this);
    }

    private void askForPlayerRepartition() {
        if (gameState == GameState.ADDING_PLAYERS) {
            view.promptForRepartition();
        }
    }

    public void createPlayers(int nbPlayers, int nbAIs) {
        int i = 1;
        while (players.size() < nbPlayers) {
            view.promptForPlayerName(i);
            if (players.size() >= i - 1) i++;
        }
        for (i = nbPlayers; i < nbPlayers + nbAIs; i++) {
            players.add(new AI());
        }
        gameState = GameState.PLAYERS_ADDED;
    }

    public void addPlayer(String playerName) { //TODO Make sure that names are unique
        if (gameState == GameState.ADDING_PLAYERS) {
            players.add(new Player(playerName));
        }
    }

    public void setupGame() {
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
                case BROOMSTICK, WART -> huntEffect.add(new ChooseNextEffect());
                case DUCKING_STOOL -> huntEffect.add(new RevealOrDiscardEffect());
                case CAULDRON, TOAD -> huntEffect.add(new RevealOwnIdentityEffect());
                case EVIL_EYE -> {
                    huntEffect.add(new ChooseNextEffect());
                    huntEffect.add(new NextMustAccuseOtherEffect());
                }
                case BLACK_CAT -> huntEffect.add(new DiscardedToHandEffect());
                case PET_NEWT -> huntEffect.add(new TakeRevealedFromOtherEffect());
            }

            deck.add(new RumourCard(cardName, witchEffects, huntEffect));
        }
        gameState = GameState.GAME_INITIATED;
    }

    public void nextAction(String nextChoice) {
        gameState = "q".equals(nextChoice) ? GameState.END_OF_SESSION : GameState.GAME_INITIATED;
    }

    public void run() {
        while (gameState != GameState.END_OF_SESSION) {
            switch (gameState) {
                case ADDING_PLAYERS -> askForPlayerRepartition();
                case PLAYERS_ADDED -> setupGame();
                case GAME_INITIATED -> {
                    if (!verifyScores()) {
                        RoundController roundController = new RoundController(this, view);
                        roundController.run();
                    } else {
                        gameState = GameState.GAME_COMPLETED;
                    }
                }
                case GAME_COMPLETED -> {
                    wrapUpGame();
                    view.promptForNewGame();
                }
            }
        }
    }

    private boolean verifyScores() {
        for (Player player : players)
            if (player.getScore() >= 5) return true;
        return false;
    }

    private void wrapUpGame() {
        List<Player> winners = new ArrayList<>();

        for (Player player : players) {
            if (player.getScore() >= 5) winners.add(player);
            player.resetScore();
        }

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
     * Get random integer in a given interval
     * This function is a utility function used to get a random integer between 2 limits (included)
     *
     * @param min the minimum value to be returned (included)
     * @param max the maximum value to be returned (included)
     * @return random integer in the interval
     */
    public static int randomInInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt((max + 1) - min) + min;
    }

    private static final String[] NAMES = {"Jean", "Antoine", "Fabrice", "Patrick", "Clara", "June", "Louis", "Silvain"};

    /**
     * Get a random not already assigned name
     *
     * @return new name
     */
    public static String randomAIName() {
        String name;
        boolean nameAssigned;
        do {
            name = NAMES[GameController.randomInInterval(0, NAMES.length - 1)];
            nameAssigned = false; //TODO Check if the name is already assigned
        } while (nameAssigned);
        return name;
    }

    public static void main(String[] args) {
        GameController gameController = new GameController(new CommandLineView());
        gameController.run();
    }
}
