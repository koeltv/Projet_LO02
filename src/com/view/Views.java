package com.view;

import com.controller.GameController;
import com.model.card.CardName;
import com.model.player.PlayerAction;

import java.util.ArrayList;
import java.util.List;

public class Views implements View {

    final List<View> views;

    public View activeView;

    public Views() {
        views = new ArrayList<>();
    }

    public void addView(View view) {
        if (activeView == null) activeView = view;
        views.add(view);
    }

    public void switchActiveView(View view) {
        if (views.contains(view)) {
            this.activeView = view;
        }
    }

    @Override
    public void setController(GameController gameController) {
        views.forEach(view -> view.setController(gameController));
    }

    @Override
    public void promptForPlayerName(int playerIndex) {
        views.forEach(view -> view.promptForPlayerName(playerIndex));
    }

    @Override
    public boolean promptForNewGame() {
        views.stream().filter(view -> view != activeView).forEach(View::promptForNewGame);
        return activeView.promptForNewGame();
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        views.stream().filter(view -> view != activeView).forEach(view -> view.promptForPlayerChoice(playerNames));
        return activeView.promptForPlayerChoice(playerNames);
    }

    @Override
    public int promptForCardChoice(List<CardName> rumourCardNames) {
        views.stream().filter(view -> view != activeView).forEach(view -> view.promptForCardChoice(rumourCardNames));
        return activeView.promptForCardChoice(rumourCardNames);
    }

    @Override
    public void promptForRepartition() {
        views.forEach(View::promptForRepartition);
    }

    @Override
    public void showGameWinner(String name, int numberOfRound) {
        views.forEach(view -> view.showGameWinner(name, numberOfRound));
    }

    @Override
    public void showRoundWinner(String name) {
        views.forEach(view -> view.showRoundWinner(name));
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        views.forEach(view -> view.showStartOfRound(numberOfRound));
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        views.stream().filter(view -> view != activeView).forEach(view -> view.promptForPlayerIdentity(name));
        return activeView.promptForPlayerIdentity(name);
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        views.stream().filter(view -> view != activeView).forEach(view -> view.promptForAction(playerName, possibleActions));
        return activeView.promptForAction(playerName, possibleActions);
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        views.forEach(view -> view.showPlayerIdentity(name, witch));
    }

    @Override
    public void showCurrentPlayer(String name) {
        views.forEach(view -> view.showCurrentPlayer(name));
    }

    @Override
    public void showPlayerAction(String name) {
        views.forEach(view -> view.showPlayerAction(name));
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        views.forEach(view -> view.showPlayerAction(name, targetedPlayerName));
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        views.forEach(view -> view.showPlayerAction(name, chosenCardName));
    }
}
