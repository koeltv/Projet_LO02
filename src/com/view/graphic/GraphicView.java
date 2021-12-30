package com.view.graphic;

import com.controller.PlayerAction;
import com.model.card.RumourCard;
import com.view.ActiveView;
import com.view.PassiveView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Graphic view.
 * 
 * Gives all the methods related to the graphic view.
 */
public abstract class GraphicView extends JFrame implements PassiveView, ActiveView {

    /**
     * Instantiates a new Graphic view.
     */
    public GraphicView() {
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
     * Prompt for integer.
     *
     * @param title   the title
     * @param message the message
     * @return the int
     */
    private int promptForInt(String title, String message) {
        String input = promptForInput(title, message);
        while (!input.matches("\\d+")) {
            System.err.println("Input format not correct, integer is needed");
            input = promptForInput(title, message);
        }
        return Integer.parseInt(input);
    }

    /**
     * Prompt player with input box.
     *
     * @param title   title of the input box
     * @param message message to display
     * @return user input
     */
    String promptForInput(String title, String message) {
        Object input = JOptionPane.showInputDialog(
                this,
                message,
                title, JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                ""
        );
        if (input instanceof String s) {
            return s;
        } else {
            System.exit(0);
            return null;
        }
    }

    /**
     * Prompt for options.
     *
     * @param title   the title of the box
     * @param message the message of the box
     * @param options the options to propose
     * @return the index of the chosen option
     */
    int promptForOptions(String title, String message, String[] options) {
        int input = JOptionPane.showOptionDialog(
                rootPane,
                message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]
        );
        if (input == JOptionPane.CLOSED_OPTION) System.exit(0);
        return input;
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
        return promptForOptions("Player choice", "Choose a player", playerNames.toArray(String[]::new));
    }

    @Override
    public int promptForCardChoice(List<RumourCard> rumourCards) {
        String[] cardNames = rumourCards.stream().map(rumourCard -> rumourCard.getCardName().toString()).toArray(String[]::new);
        return promptForOptions("Card choice", "Choose a card", cardNames);
    }

    @Override
    public int promptForCardChoice(int listSize) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < listSize; i++) strings.add("Card n°" + i);
        return promptForOptions("Card choice", "Choose a card", strings.toArray(String[]::new));
    }

    @Override
    public int[] promptForRepartition() {
        int nbPlayers = promptForInt("Number of players", "Number of players ?");
        int nbAIs = promptForInt("Number of AIs", "Number of AIs ?");
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

    @Override
    public void promptForPlayerSwitch(String name) {
        promptForOptions("Player Switch", "Please pass the hand to Player " + name, new String[]{"Done"});
    }
}
