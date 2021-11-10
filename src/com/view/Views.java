package com.view;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.PlayerAction;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Views.
 * Contain a list of passive views and 1 active view. Made to be able to handle more than 1 view at a time.
 */
public class Views implements ActiveView {

    final List<PassiveView> views;

    public ActiveView activeView;

    public Views(ActiveView activeView) {
        this.views = new ArrayList<>();

        this.activeView = activeView;
    }

    public void addView(View view) {
        if (view instanceof PassiveView) views.add((PassiveView) view);
    }

    public void switchActiveView(ActiveView view) {
        this.activeView = view;
    }

    @Override
    public void showGameWinner(String name, int numberOfRound) {
        views.forEach(view -> view.showGameWinner(name, numberOfRound));
        activeView.showGameWinner(name, numberOfRound);
    }

    @Override
    public void showRoundWinner(String name) {
        views.forEach(view -> view.showRoundWinner(name));
        activeView.showRoundWinner(name);
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        views.forEach(view -> view.showStartOfRound(numberOfRound));
        activeView.showStartOfRound(numberOfRound);
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        views.forEach(view -> view.showPlayerIdentity(name, witch));
        activeView.showPlayerIdentity(name, witch);
    }

    @Override
    public void showPlayerAction(String name) {
        views.forEach(view -> view.showPlayerAction(name));
        activeView.showPlayerAction(name);
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        views.forEach(view -> view.showPlayerAction(name, targetedPlayerName));
        activeView.showPlayerAction(name, targetedPlayerName);
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        views.forEach(view -> view.showPlayerAction(name, chosenCardName));
        activeView.showPlayerAction(name, chosenCardName);
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
    public int promptForCardChoice(List<RumourCard> rumourCards) {
        views.forEach(passiveView -> passiveView.waitForCardChoice(rumourCards));
        return activeView.promptForCardChoice(rumourCards);
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
