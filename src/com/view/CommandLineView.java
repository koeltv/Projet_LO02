package com.view;

import com.controller.PlayerAction;
import com.model.card.RumourCard;

import java.util.List;
import java.util.Scanner;

/**
 * The type Command line view.
 * 
 * Gives all the methods related to the command line view.
 */
public class CommandLineView implements PassiveView, ActiveView {
    
	/**
     * The Keyboard.
     */
    final Scanner keyboard = new Scanner(System.in);

    @Override
    public String toString() {
        return "Command Line View";
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void showGameWinner(String name, int numberOfRound) {
        System.out.println("\n-----------------------------------------------");
        System.out.println("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");
        System.out.println("-----------------------------------------------\n");
    }

    @Override
    public void showRoundWinner(String name) {
        System.out.println(name + " won this round !");
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        System.out.println("================Round " + numberOfRound + "================");
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        System.out.println(name + " is a " + (witch ? "witch" : "villager") + " !");
    }

    @Override
    public void showRevealAction(String name) {
        System.out.println("Player " + name + " is revealing his identity !");
    }

    @Override
    public void showAccuseAction(String name, String targetedPlayerName) {
        System.out.println("Player " + name + " is accusing " + targetedPlayerName + " !");
    }

    @Override
    public void showUseCardAction(String name, String chosenCardName) {
        System.out.println("Player " + name + " is using " + chosenCardName + " !");
    }

    @Override
    public void showCardList(String name, List<String> cards) {
        System.out.println("================ " + name + " ================\n");
        for (String cardDescription : cards) {
            System.out.println(cardDescription);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Passive Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void waitForPlayerName(int playerIndex) {
        System.out.println("Waiting for Player " + playerIndex + " name ...");
    }

    @Override
    public void waitForNewGame() {
        System.out.println("Waiting for a new game choice");
    }

    @Override
    public void waitForPlayerChoice(List<String> playerNames) {
        System.out.println("Waiting for player choice");
        playerNames.forEach(System.out::println);
    }

    @Override
    public void waitForCardChoice(List<RumourCard> rumourCards) {
        System.out.println("Waiting for card choice");
        if (rumourCards != null) rumourCards.forEach(System.out::println);
    }

    @Override
    public void waitForRepartition() {
        System.out.println("Waiting for repartition choice");
    }

    @Override
    public void waitForPlayerIdentity(String name) {
        System.out.println("Waiting for " + name + " identity choice");
    }

    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        System.out.println("Waiting for " + playerName + " action choice");
        possibleActions.forEach(System.out::println);
    }

    @Override
    public void waitForPlayerSwitch(String name) {
        System.out.println("Waiting for Player " + name);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Prompt for integer.
     *
     * @return the int
     */
    private int promptForInt() {
        String input = keyboard.nextLine();
        while (!input.matches("\\d+")) {
            System.err.println("Input format not correct, integer is needed");
            input = keyboard.nextLine();
        }
        return Integer.parseInt(input);
    }

    /**
     * Prompt for integer inferior to max.
     *
     * @param max the max
     * @return the int
     */
    private int promptForInt(int max) {
        String input = keyboard.nextLine();
        while (!input.matches("\\d+") || Integer.parseInt(input) > max) {
            System.err.println("Input format not correct, integer between 0 and " + max + " is needed");
            input = keyboard.nextLine();
        }
        return Integer.parseInt(input);
    }

    @Override
    public String promptForPlayerName(int playerIndex) {
        System.out.println(">> Enter player " + playerIndex + " name");
        return keyboard.nextLine();
    }

    @Override
    public String promptForNewGame() {
        System.out.println(">> Press enter to play again, q to exit or r to reset");
        return keyboard.nextLine();
    }

    @Override
    public int promptForPlayerChoice(String playerName, List<String> playerNames) {
        System.out.println(">> Choose a player by index");
        for (int i = 0; i < playerNames.size(); i++) {
            System.out.println(i + "- " + playerNames.get(i));
        }
        return promptForInt(playerNames.size() - 1);
    }

    @Override
    public int promptForCardChoice(String playerName, List<RumourCard> rumourCards) {
        System.out.println(">> Choose a card by index");
        for (int i = 0; i < rumourCards.size(); i++) {
            System.out.println(i + "- " + rumourCards.get(i));
        }
        return promptForInt(rumourCards.size() - 1);
    }

    @Override
    public int promptForCardChoice(String playerName, int listSize) {
        System.out.println(">> Choose a card by index");
        for (int i = 0; i < listSize; i++) System.out.println("Card n??" + i);
        return promptForInt(listSize - 1);
    }

    @Override
    public int[] promptForRepartition() {
        System.out.println(">> Number of players ?");
        int nbPlayers = promptForInt();
        System.out.println(">> Number of AI ?");
        int nbAIs = promptForInt();
        return new int[]{nbPlayers, nbAIs};
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        System.out.println(">> " + name + ", type 0 for villager and 1 for witch");
        return promptForInt();
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        System.out.println(">> " + playerName + ", please choose your next action");
        for (int i = 0; i < possibleActions.size(); i++) {
            System.out.println(i + "- " + possibleActions.get(i));
        }
        return possibleActions.get(promptForInt(possibleActions.size() - 1));
    }

    @Override
    public void promptForPlayerSwitch(String name) {
        System.out.println(">> Please pass the hand to Player " + name + ", then press enter");
        keyboard.nextLine();
    }
}
