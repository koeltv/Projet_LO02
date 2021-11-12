package com.view.graphic.dynamic;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;
import com.model.player.PlayerAction;
import com.view.ActiveView;
import com.view.graphic.GraphicView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Graphical 2D view.
 * Graphical view which display the whole game in 2D graphics. It is resizable.
 */
public class Graphical2DView extends GraphicView implements ActiveView {
    private final Panel panel;

    private int waitingTime;

    /**
     * Instantiates a new Graphical 2D view.
     */
    public Graphical2DView() {
        super();
        //Create main frame
        panel = new Panel();
        this.setContentPane(panel);
        this.setSize(750, 400);

        this.waitingTime = 10;

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

    ///////////////////////////////////////////////////////////////////////////
    // View Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public synchronized void showGameWinner(String name, int numberOfRound) {
        panel.displayAction("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");
        run(true);

        //We restart the thread to stop the panel from trying to access the deleted RoundController

    }

    @Override
    public void showRoundWinner(String name) {
        panel.displayAction(name + " won this round !");
        run(true);
    }

    @Override
    public void showStartOfRound(int numberOfRound) {
        panel.displayAction("Start of Round " + numberOfRound);
        run(true);
    }

    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        panel.displayAction(name + " is a " + (witch ? "witch" : "villager") + " !");
        run(true);
    }

    @Override
    public void showPlayerAction(String name) {
        panel.displayAction("Player " + name + " is revealing his identity !");
        run(true);
    }

    @Override
    public void showPlayerAction(String name, String targetedPlayerName) {
        panel.displayAction("Player " + name + " is accusing " + targetedPlayerName + " !");
        run(true);
    }

    @Override
    public void showPlayerAction(String name, CardName chosenCardName) {
        panel.displayAction("Player " + name + " is using " + chosenCardName + " !");
        run(true);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Passive Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void waitForPlayerName(int playerIndex) {
        run(false);
    }

    @Override
    public void waitForNewGame() {
        run(false);
    }

    @Override
    public void waitForPlayerChoice(List<String> playerNames) {
        run(false);
    }

    @Override
    public void waitForCardChoice(List<RumourCard> rumourCards) {
        run(false);
    }

    @Override
    public void waitForRepartition() {
        run(false);
    }

    @Override
    public void waitForPlayerIdentity(String name) {
        actualiseMainPlayer(name);
        run(false);
    }

    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
        run(false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
        run(true);
        return super.promptForAction(playerName, possibleActions);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Runnable method
    ///////////////////////////////////////////////////////////////////////////

    public synchronized void run(boolean wait) {
        try {
            //repaint is called each time the screen size is changed or by this loop
            panel.repaint();
            if (wait) {
                wait(waitingTime);
            }
            waitingTime = panel.getWaitingTime();
        } catch (InterruptedException ignored) {
            //When we restart the thread, an exception is thrown, see https://stackoverflow.com/questions/35474536/wait-is-always-throwing-interruptedexception
        }
    }
}
