package com.view;

import com.model.card.CardName;
import com.model.player.PlayerAction;

import java.util.List;
import java.util.Scanner;

public class CommandLineView implements ActiveView, PassiveView {
    final Scanner keyboard = new Scanner(System.in);

    @Override
    public void showGameWinner(String name, int numberOfRound) {
        System.out.println("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");
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
        System.out.print(name + " is a ");
        System.out.println(witch ? "witch !" : "villager !");
    }

    @Override
    public void showCurrentPlayer(String name) {
        System.out.println("This is now " + name + "'s turn !");
    }

    @Override
    public void showPlayerAction(String name) {
        System.out.println("Player " + name + " is revealing his identity !");
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        System.out.println("Player " + name + " is accusing " + targetedPlayerName + " !");
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        System.out.println("Player " + name + " is using " + chosenCardName + " !");
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
    public void waitForCardChoice(List<CardName> rumourCardNames) {
        System.out.println("Waiting for card choice");
        rumourCardNames.forEach(System.out::println);
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

    ///////////////////////////////////////////////////////////////////////////
    // Active Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public String promptForPlayerName(int playerIndex) {
        System.out.println("Enter player " + playerIndex + " name");
        return keyboard.nextLine();
    }

    @Override
    public String promptForNewGame() {
        System.out.println("Press enter to play again or q to exit");
        return keyboard.nextLine();
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        System.out.println("Choose a player by index");
        for (int i = 0; i < playerNames.size(); i++) {
            System.out.println(i + "- " + playerNames.get(i));
        }
        return Integer.parseInt(keyboard.nextLine());
    }

    @Override
    public int promptForCardChoice(List<CardName> rumourCardNames) {
        System.out.println("Choose a card by index");
        for (int i = 0; i < rumourCardNames.size(); i++) {
            System.out.println(i + "- " + rumourCardNames.get(i));
        }
        return Integer.parseInt(keyboard.nextLine());
    }

    @Override
    public int[] promptForRepartition() {
        System.out.println("Number of players ?");
        int nbPlayers = Integer.parseInt(keyboard.nextLine());
        System.out.println("Number of AI ?");
        int nbAIs = Integer.parseInt(keyboard.nextLine());
        return new int[]{nbPlayers, nbAIs};
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        System.out.println(name + ", type 0 for villager and 1 for witch");
        return Integer.parseInt(keyboard.nextLine());
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        System.out.println(playerName + ", please choose your next action");
        for (int i = 0; i < possibleActions.size(); i++) {
            System.out.println(i + "- " + possibleActions.get(i));
        }
        return possibleActions.get(Integer.parseInt(keyboard.nextLine()));
    }
}
