package com.view;

import com.controller.GameController;
import com.model.card.CardName;

import java.util.List;
import java.util.Scanner;

public class CommandLineView implements View {
    GameController controller;
    final Scanner keyboard = new Scanner(System.in);

    @Override
    public void setController(GameController gameController) {
        this.controller = gameController;
    }

    @Override
    public void promptForPlayerName(int playerIndex) {
        System.out.println("Enter player " + playerIndex + " name");
        controller.addPlayer(keyboard.next());
    }

    @Override
    public void promptForNewGame() {
        System.out.println("Press enter to deal again or q to exit");
        controller.nextAction(keyboard.next());
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        System.out.println("Choose a player by index");
        for (int i = 0; i < playerNames.size(); i++) {
            System.out.println(i + "- " + playerNames.get(i));
        }
        return keyboard.nextInt();
    }

    @Override
    public int promptForCardChoice(List<CardName> rumourCardNames) {
        System.out.println("Choose a card by index");
        for (int i = 0; i < rumourCardNames.size(); i++) {
            System.out.println(i + "- " + rumourCardNames.get(i));
        }
        return keyboard.nextInt();
    }

    @Override
    public void promptForRepartition() {
        System.out.println("Number of players ?");
        int nbPlayers = keyboard.nextInt();
        System.out.println("Number of AI ?");
        int nbAIs = keyboard.nextInt();
        if (nbPlayers + nbAIs >= 3 && nbPlayers + nbAIs <= 6) {
            controller.createPlayers(nbPlayers, nbAIs);
        }
    }

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
    public int promptForPlayerIdentity(String name) {
        System.out.println(name + ", type 0 for villager and 1 for witch");
        return keyboard.nextInt();
    }

    @Override
    public String promptForAction(String playerName, List<String> possibleActions) {
        System.out.println(playerName + ", please choose your next action");
        for (int i = 0; i < possibleActions.size(); i++) {
            System.out.println(i + "- " + possibleActions.get(i));
        }
        return possibleActions.get(keyboard.nextInt());
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        System.out.print(name + " is a ");
        System.out.println(witch ? "witch !" : "villager !");
    }
}
