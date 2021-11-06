package com.view;

import com.model.card.CardName;
import com.model.player.PlayerAction;

import java.util.ArrayList;
import java.util.List;

public class Views implements ActiveView {

    final List<PassiveView> views;

    public ActiveView activeView;

    public Views(ActiveView activeView) {
        this.views = new ArrayList<>();

        this.activeView = activeView;
    }

    public void addView(View view) {
        if (activeView == null && view instanceof ActiveView) activeView = (ActiveView) view;
        else if (view instanceof PassiveView) views.add((PassiveView) view);
    }

    public void switchActiveView(ActiveView view) {
        this.activeView = view;
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

    ///////////////////////////////////////////////////////////////////////////
    // Active Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public String promptForPlayerName(int playerIndex) {
        views.forEach(view -> view.waitForPlayerName(playerIndex));
        return activeView.promptForPlayerName(playerIndex);
    }

    @Override
    public String promptForNewGame() {
        views.forEach(PassiveView::waitForNewGame);
        return activeView.promptForNewGame();
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        views.forEach(passiveView -> passiveView.waitForPlayerChoice(playerNames));
        return activeView.promptForPlayerChoice(playerNames);
    }

    @Override
    public int promptForCardChoice(List<CardName> rumourCardNames) {
        views.forEach(passiveView -> passiveView.waitForCardChoice(rumourCardNames));
        return activeView.promptForCardChoice(rumourCardNames);
    }

    @Override
    public int[] promptForRepartition() {
        views.forEach(PassiveView::waitForRepartition);
        return activeView.promptForRepartition();
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        views.forEach(passiveView -> passiveView.waitForPlayerIdentity(name));
        return activeView.promptForPlayerIdentity(name);
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        views.forEach(passiveView -> passiveView.waitForAction(playerName, possibleActions));
        return activeView.promptForAction(playerName, possibleActions);
    }
}
