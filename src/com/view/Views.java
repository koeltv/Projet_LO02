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
        for (View view : views) {
            view.promptForPlayerName(playerIndex);
        }
    }

    @Override
    public boolean promptForNewGame() {
        for (View view : views) {
            if (view != activeView) view.promptForNewGame();
        }
        return activeView.promptForNewGame();
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        for (View view : views) {
            if (view != activeView) view.promptForPlayerChoice(playerNames);
        }
        return activeView.promptForPlayerChoice(playerNames);
    }

    @Override
    public int promptForCardChoice(List<CardName> rumourCardNames) {
        for (View view : views) {
            if (view != activeView) view.promptForCardChoice(rumourCardNames);
        }
        return activeView.promptForCardChoice(rumourCardNames);
    }

    @Override
    public void promptForRepartition() {
        for (View view : views) {
            view.promptForRepartition();
        }
    }

    @Override
    public void showGameWinner(String name, int numberOfRound) {
        for (View view : views) {
            view.showGameWinner(name, numberOfRound);
        }
    }

    @Override
    public void showRoundWinner(String name) {
        for (View view : views) {
            view.showRoundWinner(name);
        }
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        for (View view : views) {
            view.showStartOfRound(numberOfRound);
        }
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        for (View view : views) {
            if (view != activeView) view.promptForPlayerIdentity(name);
        }
        return activeView.promptForPlayerIdentity(name);
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        for (View view : views) {
            if (view != activeView) view.promptForAction(playerName, possibleActions);
        }
        return activeView.promptForAction(playerName, possibleActions);
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        for (View view : views) {
            view.showPlayerIdentity(name, witch);
        }
    }

    @Override
    public void showCurrentPlayer(String name) {
        for (View view : views) {
            view.showCurrentPlayer(name);
        }
    }

    @Override
    public void showPlayerAction(String name) {
        for (View view : views) {
            view.showPlayerAction(name);
        }
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        for (View view : views) {
            view.showPlayerAction(name, targetedPlayerName);
        }
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        for (View view : views) {
            view.showPlayerAction(name, chosenCardName);
        }
    }
}
