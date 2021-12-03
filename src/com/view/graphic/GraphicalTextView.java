package com.view.graphic;

import com.controller.PlayerAction;
import com.model.card.CardName;
import com.model.card.RumourCard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The type Graphical Text View.
 * Made to display user's interaction in a graphical interface using a text window and input boxes.
 */
public class GraphicalTextView extends GraphicView {
    /**
     * The Text area.
     */
    private JTextArea textArea;

    /**
     * Instantiates a new Graphical text view.
     */
    public GraphicalTextView() {
        super();
        //Create main frame
        this.setSize(350, 500);

        //Display vertically
        Container contentPane = this.getContentPane();
        addControllerCommandTracker(contentPane);

        this.setVisible(true);
    }

    @Override
    public String toString() {
        return "Graphical Text View";
    }

    /**
     * A simple place to display what the controller is telling, very similar to the command line version.
     *
     * @param contentPane the container
     */
    private void addControllerCommandTracker(Container contentPane) {
        textArea = new JTextArea("Game Status\n", 100, 1);
        JScrollPane scrollPane = new JScrollPane(textArea);
        addCenteredComponent(scrollPane, contentPane);
        textArea.setSize(350, 500);
    }

    /**
     * All controls are added, so they are centered horizontally in the area.
     *
     * @param component   the component to add
     * @param contentPane the container in which to place the component
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

    ///////////////////////////////////////////////////////////////////////////
    // View Methods
    ///////////////////////////////////////////////////////////////////////////

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
    public void waitForCardChoice(List<RumourCard> rumourCards) {
        appendText("Waiting for card choice");
        if (rumourCards != null) rumourCards.forEach(rumourCard -> appendText(rumourCard.toString()));
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

    @Override
    public void waitForPlayerSwitch(String name) {
        appendText("Waiting for Player " + name);
    }

	@Override
	public void showCardList(String name, List<String> card) {
		//Nothing
	}
}
