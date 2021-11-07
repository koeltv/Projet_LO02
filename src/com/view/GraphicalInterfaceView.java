package com.view;

import com.model.card.CardName;
import com.model.player.PlayerAction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The type Graphical Interface View.
 * Made to display user's interaction in a graphical interface using a text window and input boxes.
 */
public class GraphicalInterfaceView implements PassiveView, ActiveView {
    JTextArea textArea;
    JFrame frame;

    public GraphicalInterfaceView() {
        //Create main frame
        frame = new JFrame("WitchHunt-Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        //Display vertically
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        addControllerCommandTracker(contentPane);

        frame.setVisible(true);
    }

    /**
     * A simple place to display what the controller is telling, very similar to the command line version.
     */
    private void addControllerCommandTracker(Container contentPane) {
        textArea = new JTextArea("Game Status\n", 100, 1);
        JScrollPane scrollPane = new JScrollPane(textArea);
        addCenteredComponent(scrollPane, contentPane);
        textArea.setSize(500, 500);
    }

    /**
     * All controls are added, so they are centered horizontally in the area.
     */
    private void addCenteredComponent(JComponent component, Container contentPane) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(component);
    }

    /**
     * Print text in the text area.
     *
     * @param text text to print
     */
    private void appendText(String text) {
        textArea.append(text + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    private String prompt(String title, String message) {
        return (String) JOptionPane.showInputDialog(
                frame,
                message,
                title, JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
        );
    }

    @Override
    public void showGameWinner(String name, int numberOfRound) {
        appendText("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");
    }

    @Override
    public void showRoundWinner(String name) {
        appendText(name + " won this round !");
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        appendText("================Round " + numberOfRound + "================");
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        appendText(name + " is a " + (witch ? "witch" : "villager") + " !");
    }

    @Override
    public void showCurrentPlayer(String name) {
        appendText("This is now " + name + "'s turn !");
    }

    @Override
    public void showPlayerAction(String name) {
        appendText("Player " + name + " is revealing his identity !");
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        appendText("Player " + name + " is accusing " + targetedPlayerName + " !");
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        appendText("Player " + name + " is using " + chosenCardName + " !");
    }


    ///////////////////////////////////////////////////////////////////////////
    // Passive Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void waitForPlayerName(int playerIndex) {
        appendText("Waiting for Player " + playerIndex + " name ...");
    }

    @Override
    public void waitForNewGame() {
        appendText("Waiting for a new game choice");
    }

    @Override
    public void waitForPlayerChoice(List<String> playerNames) {
        appendText("Waiting for player choice");
        playerNames.forEach(this::appendText);
    }

    @Override
    public void waitForCardChoice(List<String> rumourCardDescriptions) {
        appendText("Waiting for card choice");
        rumourCardDescriptions.forEach(this::appendText);
    }

    @Override
    public void waitForRepartition() {
        appendText("Waiting for repartition choice");
    }

    @Override
    public void waitForPlayerIdentity(String name) {
        appendText("Waiting for " + name + " identity choice");
    }

    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        appendText("Waiting for " + playerName + " action choice");
        possibleActions.forEach(possibleAction -> appendText(possibleAction.toString()));
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
