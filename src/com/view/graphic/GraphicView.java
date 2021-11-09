package com.view.graphic;

import com.model.player.PlayerAction;
import com.view.ActiveView;
import com.view.PassiveView;

import javax.swing.*;
import java.util.List;

public abstract class GraphicView extends JFrame implements PassiveView, ActiveView {
    /**
     * Prompt player with input box.
     *
     * @param title   title of the input box
     * @param message message to display
     * @return user input
     */
    String prompt(String title, String message) {
        return (String) JOptionPane.showInputDialog(
                this,
                message,
                title, JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
        );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public String promptForPlayerName(int playerIndex) {
        return prompt("", "Enter player " + playerIndex + " name");
    }

    @Override
    public String promptForNewGame() {
        return prompt("New game", "Press enter to play again, q to exit or r to reset");
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        StringBuilder message = new StringBuilder();
        message.append("Choose a player by index\n");
        for (int i = 0; i < playerNames.size(); i++) {
            message.append(i).append("- ").append(playerNames.get(i)).append("\n");
        }

        return Integer.parseInt(prompt("Player choice", message.toString()));
    }

    @Override
    public int promptForCardChoice(List<String> rumourCardDescriptions) {
        StringBuilder message = new StringBuilder();
        message.append("Choose a card by index\n");
        for (int i = 0; i < rumourCardDescriptions.size(); i++) {
            message.append(i).append("- ").append(rumourCardDescriptions.get(i)).append("\n");
        }

        return Integer.parseInt(prompt("Card choice", message.toString()));
    }

    @Override
    public int[] promptForRepartition() {
        int nbPlayers = Integer.parseInt(prompt("Number of players", "Number of players ?"));
        int nbAIs = Integer.parseInt(prompt("Number of AIs", "Number of AIs ?"));
        return new int[]{nbPlayers, nbAIs};
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        return Integer.parseInt(prompt("Identity", name + ", type 0 for villager and 1 for witch"));
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        StringBuilder message = new StringBuilder();
        message.append(playerName).append(", please choose your next action");
        for (int i = 0; i < possibleActions.size(); i++) {
            message.append(i).append("- ").append(possibleActions.get(i)).append("\n");
        }

        int index = Integer.parseInt(prompt("Action", message.toString()));
        return possibleActions.get(index);
    }
}
