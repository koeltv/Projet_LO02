package com.view.graphic;

import com.model.card.CardName;
import com.model.player.PlayerAction;
import com.view.ActiveView;
import com.view.PassiveView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class GraphicView extends JFrame implements ActiveView, PassiveView, Runnable {
    private final Panel panel = new Panel();

    public GraphicView() {
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

        Thread thread = new Thread(this);
        thread.start();
    }

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
    public void showGameWinner(String name, int numberOfRound) {

    }

    @Override
    public void showRoundWinner(String name) {

    }

    @Override
    public void showStartOfRound(int numberOfRound) {

    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {

    }

    @Override
    public void showCurrentPlayer(String name) {

    }

    @Override
    public void showPlayerAction(String name) {

    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {

    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {

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

    }

    ///////////////////////////////////////////////////////////////////////////
    // Active Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public String promptForPlayerName(int playerIndex) {
        return null;
    }

    @Override
    public String promptForNewGame() {
        return null;
    }

    @Override
    public int promptForPlayerChoice(List<String> playerNames) {
        return 0;
    }

    @Override
    public int promptForCardChoice(List<String> rumourCardDescriptions) {
        return 0;
    }

    @Override
    public int[] promptForRepartition() {
        return new int[0];
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        return 0;
    }

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        return null;
    }

    @Override
    public void run() {
        while (true) panel.repaint();
    }
}
