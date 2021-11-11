package com.view.graphic;

import com.model.card.RumourCard;
import com.model.player.PlayerAction;
import com.view.ActiveView;
import com.view.PassiveView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class GraphicView extends JFrame implements PassiveView, ActiveView {

    GraphicView() {
        this.setTitle("Witch Hunt");
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //We specify that we don't want the access to the window to be shut off when a JOptionPane appear
        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Prompt player with input box.
     *
     * @param title   title of the input box
     * @param message message to display
     * @return user input
     */
    String promptForInput(String title, String message) {
        return (String) JOptionPane.showInputDialog(
                this,
                message,
                title, JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                ""
        );
    }

    int promptForOptions(String title, String message, String[] options) {
        return JOptionPane.showOptionDialog(
                rootPane,
                message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]
        );
    }

    @Override
    public String promptForPlayerName(int playerIndex) {
        return promptForInput("", "Enter player " + playerIndex + " name");
    }

    @Override
    public String promptForNewGame() {
        String[] options = {"Yes", "No", "Reset"};
        String choice = options[promptForOptions("New game", "Do you want to start a new game ?", options)];
        return switch (choice) {
            case "No" -> "q";
            case "Reset" -> "r";
            default -> "";
        };
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        return promptForOptions("Player choice", "Choose a player", playerNames.toArray(new String[0]));
    }

    @Override
    public int promptForCardChoice(List<RumourCard> rumourCards) {
        String[] cardNames = rumourCards.stream().map(rumourCard -> rumourCard.getCardName().toString()).toArray(String[]::new);
        return promptForOptions("Card choice", "Choose a card", cardNames);
    }

    @Override
    public int[] promptForRepartition() {
        int nbPlayers = Integer.parseInt(promptForInput("Number of players", "Number of players ?"));
        int nbAIs = Integer.parseInt(promptForInput("Number of AIs", "Number of AIs ?"));
        return new int[]{nbPlayers, nbAIs};
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        String[] options = {"Villager", "Witch"};
        return promptForOptions("Identity choice", name + ", choose your identity", options);
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        String[] actions = possibleActions.stream().map(Enum::toString).toArray(String[]::new);
        return possibleActions.get(promptForOptions("Action", playerName + ", please choose your next action", actions));
    }
}
