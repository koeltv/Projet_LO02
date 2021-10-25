package com.view;

import com.controller.GameController;
import com.model.card.CardName;
import com.model.player.PlayerAction;

import java.util.List;

public interface View {
    void setController(GameController gameController);

    void promptForPlayerName(int playerIndex);

    void promptForNewGame();

    int promptForPlayerChoice(List<String> playerNames);

    int promptForCardChoice(List<CardName> rumourCardNames);

    void promptForRepartition();

    void showGameWinner(String name, int numberOfRound);

    void showRoundWinner(String name);

    void showStartOfRound(int numberOfRound);

    int promptForPlayerIdentity(String name);

    PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions);

    void showPlayerIdentity(String name, boolean witch);

    void showCurrentPlayer(String name);

    void showPlayerAction(String name);

    void showPlayerAction(String name, String targetedPlayerName);

    void showPlayerAction(String name, CardName chosenCardName);
}
