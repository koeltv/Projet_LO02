package com.view.graphic;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.game.IdentityCard;
import com.model.player.PlayerAction;
import com.view.ActiveView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
        //Create main frame
        this.setTitle("Witch Hunt");
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 400);

        //Display vertically
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        this.addMouseMotionListener(mouseMotionListener);
        this.setVisible(true);

        thread = new Thread(this);
        thread.start();
    }

    /**
     * The Mouse motion listener.
     * Used to get mouse position on click. Not used right now.
     */
    MouseMotionListener mouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
            panel.mouseXPos = e.getX();
            panel.mouseYPos = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    };

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
    public void showCurrentPlayer(String name) {
        panel.displayAction("This is now " + name + "'s turn !");
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
    public void waitForCardChoice(List<String> rumourCardDescriptions) {

    }

    @Override
    public void waitForRepartition() {

    }

    @Override
    public void waitForPlayerIdentity(String name) {

    }

    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        //We change the player at the bottom of the display to the player currently playing
        List<IdentityCard> identityCards = RoundController.getRoundController().identityCards
                .stream()
                .filter(identityCard -> identityCard.player.getName().equals(playerName))
                .collect(Collectors.toList());
        panel.mainPlayer = identityCards.size() > 0 ? identityCards.get(0).player : null;
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
