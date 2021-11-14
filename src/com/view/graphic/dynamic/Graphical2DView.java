package com.view.graphic.dynamic;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;
import com.model.player.PlayerAction;
import com.view.graphic.GraphicView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Graphical 2D view.
 * Graphical view which display the whole game in 2D graphics. It is resizable.
 */
public class Graphical2DView extends GraphicView {
    private final Panel panel;

    /**
     * Instantiates a new Graphical 2D view.
     */
    public Graphical2DView() {
        super();
        //Create main frame
        panel = new Panel();
        this.setContentPane(panel);
        this.setSize(750, 400);

        this.setVisible(true);
    }

    @Override
    public String toString() {
        return "Graphical 2D View";
    }

    private void actualiseMainPlayer(String playerName) {
        //We change the player at the bottom of the display to the player currently playing
        List<IdentityCard> identityCards = RoundController.getRoundController().identityCards
                .stream()
                .filter(identityCard -> identityCard.player.getName().equals(playerName))
                .collect(Collectors.toList());
        panel.setMainPlayer(identityCards.size() > 0 ? identityCards.get(0).player : null);
    }

    public synchronized void displayAndRepaint() {
        panel.repaint();
    }

    public synchronized void displayAndRepaint(String text) {
        panel.setAction(text);
        try {
            //Repaint is called each time the screen size is changed or here
            panel.repaint();
            wait(panel.getWaitingTime());
        } catch (InterruptedException ignored) {
            //Ignored right now, could be used later
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public synchronized void showGameWinner(String name, int numberOfRound) {
        displayAndRepaint("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");
    }

    @Override
    public void showRoundWinner(String name) {
        displayAndRepaint(name + " won this round !");
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        displayAndRepaint("Start of Round " + numberOfRound);
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        displayAndRepaint(name + " is a " + (witch ? "witch" : "villager") + " !");
    }

    @Override
    public void showPlayerAction(String name) {
        displayAndRepaint("Player " + name + " is revealing his identity !");
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        displayAndRepaint("Player " + name + " is accusing " + targetedPlayerName + " !");
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        displayAndRepaint("Player " + name + " is using " + chosenCardName + " !");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Passive Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void waitForPlayerName(int playerIndex) {
        displayAndRepaint();
    }

    @Override
    public void waitForNewGame() {
        displayAndRepaint();
    }

    @Override
    public void waitForPlayerChoice(List<String> playerNames) {
        displayAndRepaint();
    }

    @Override
    public void waitForCardChoice(List<RumourCard> rumourCards) {
        displayAndRepaint();
    }

    @Override
    public void waitForRepartition() {
        displayAndRepaint();
    }

    @Override
    public void waitForPlayerIdentity(String name) {
        actualiseMainPlayer(name);
        displayAndRepaint();
    }

    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
        displayAndRepaint();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
        displayAndRepaint();
        return super.promptForAction(playerName, possibleActions);
    }
}
