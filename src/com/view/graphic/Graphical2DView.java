package com.view.graphic;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;
import com.model.player.PlayerAction;
import com.view.ActiveView;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Graphical 2D view.
 * Graphical view which display the whole game in 2D graphics. It is resizable.
 */
public class Graphical2DView extends GraphicView implements ActiveView, Runnable {
    private final Panel panel = new Panel();

    private Thread thread;

    /**
     * Instantiates a new Graphical 2D view.
     */
    public Graphical2DView() {
        super();
        //Create main frame
        this.setContentPane(panel);
        this.setSize(750, 400);

        //Display vertically
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        this.setVisible(true);

        thread = new Thread(this);
        thread.start();
    }

    private void actualiseMainPlayer(String playerName) {
        //We change the player at the bottom of the display to the player currently playing
        List<IdentityCard> identityCards = RoundController.getRoundController().identityCards
                .stream()
                .filter(identityCard -> identityCard.player.getName().equals(playerName))
                .collect(Collectors.toList());
        panel.mainPlayer = identityCards.size() > 0 ? identityCards.get(0).player : null;
    }

    @Override
    public synchronized void showGameWinner(String name, int numberOfRound) {
        panel.displayAction("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");

        //We restart the thread to stop the panel from trying to access the deleted RoundController
        thread.interrupt();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void showRoundWinner(String name) {
        panel.resetActions();
        panel.displayAction(name + " won this round !");
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        panel.displayAction("Start of Round " + numberOfRound);
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        panel.displayAction(name + " is a " + (witch ? "witch" : "villager") + " !");
    }

    @Override
    public void showPlayerAction(String name) {
        panel.displayAction("Player " + name + " is revealing his identity !");
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        panel.displayAction("Player " + name + " is accusing " + targetedPlayerName + " !");
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        panel.displayAction("Player " + name + " is using " + chosenCardName + " !");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Passive Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void waitForPlayerName(int playerIndex) {

    }

    @Override
    public void waitForNewGame() {

    }

    @Override
    public void waitForPlayerChoice(List<String> playerNames) {

    }

    @Override
    public void waitForCardChoice(List<RumourCard> rumourCards) {

    }

    @Override
    public void waitForRepartition() {

    }

    @Override
    public void waitForPlayerIdentity(String name) {

    }

    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
        return super.promptForAction(playerName, possibleActions);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Runnable method
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public synchronized void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                //repaint is called each time the screen size is changed or by this loop
                panel.repaint();
                wait(500);
            }
        } catch (InterruptedException ignored) {
            //When we restart the thread, an exception is thrown, see https://stackoverflow.com/questions/35474536/wait-is-always-throwing-interruptedexception
        }
    }
}
