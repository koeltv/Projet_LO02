package com.view.graphic.dynamic;

import com.controller.PlayerAction;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;
import com.model.game.Round;
import com.model.player.AI;
import com.view.graphic.GraphicView;

import java.util.List;

/**
 * The type Graphical 2D view.
 * 
 * Graphical view which display the whole game in 2D graphics. It is resizable.
 */
public class Graphical2DView extends GraphicView {
    
	/**
     * The Panel.
     * 
     * @see com.view.graphic.dynamic.Panel
     */
    private final Panel panel;

    /**
     * Instantiates a new Graphical 2D view.
     * 
     * @see com.view.graphic.dynamic.Panel
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

    /**
     * Actualise main player.
     *
     * @param playerName the main player name
     * @see com.model.game.IdentityCard
     * @see com.model.game.Round
     * @see com.model.player.Player
     */
    private void actualiseMainPlayer(String playerName) {
        //We change the player at the bottom of the display to the player currently playing
        List<IdentityCard> identityCards = Round.getInstance().getIdentityCards().stream()
                .filter(identityCard -> identityCard.player.getName().equals(playerName)).toList();
        if (identityCards.size() > 0 && !(identityCards.get(0).player instanceof AI)) {
            panel.setMainPlayer(identityCards.get(0).player);
        } else if (identityCards.size() <= 0) {
            panel.setMainPlayer(null);
        }
    }

    /**
     * Display and repaint.
     */
    public synchronized void displayAndRepaint() {
        panel.repaint();
    }

    /**
     * Display and repaint with text display.
     *
     * @param text the text to display
     * @see com.view.graphic.dynamic.Panel
     */
    public synchronized void displayAndRepaint(String text) {
        panel.setAction(text);
        try { //Repaint is called each time the screen size is changed or here
            panel.repaint();
            wait(panel.getWaitingTime());
        } catch (InterruptedException ignored) {
            //Ignored right now, could be used later
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Show game winner.
     * This function shows the game winner.
     */
    @Override
    public synchronized void showGameWinner(String name, int numberOfRound) {
        displayAndRepaint("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");
    }

    /**
     * Show round winner.
     * This function shows the round winner.
     */
    @Override
    public void showRoundWinner(String name) {
        displayAndRepaint(name + " won this round !");
    }

    /**
     * Show start of round.
     * This function shows the start of a new round and its number.
     */
    @Override
    public void showStartOfRound(int numberOfRound) {
        displayAndRepaint("Start of Round " + numberOfRound);
    }

    /**
     * Show player identity.
     * This function shows the player's identity.
     */
    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        displayAndRepaint(name + " is a " + (witch ? "witch" : "villager") + " !");
    }

    /**
     * Show reveal action.
     * This function shows the revelation of a player identity.
     */
    @Override
    public void showRevealAction(String name) {
        displayAndRepaint("Player " + name + " is revealing his identity !");
    }

    /**
     * Show accuse action.
     * This function shows the accusation of a targeted player.
     */
    @Override
    public void showAccuseAction(String name, String targetedPlayerName) {
        displayAndRepaint("Player " + name + " is accusing " + targetedPlayerName + " !");
    }

    /**
     * Show use card action.
     * This function shows the card which is used by the player.
     */
    @Override
    public void showUseCardAction(String name, String chosenCardName) {
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

    @Override
    public void waitForPlayerSwitch(String name) {
        actualiseMainPlayer(null);
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

    @Override
    public void promptForPlayerSwitch(String name) {
        actualiseMainPlayer(null);
        displayAndRepaint();
        super.promptForPlayerSwitch(name);
    }

    @Override
    public int promptForPlayerIdentity(String name) {
        actualiseMainPlayer(name);
        displayAndRepaint();
        return super.promptForPlayerIdentity(name);
    }

	@Override
    public void showCardList(String name, List<String> cards) {
        actualiseMainPlayer(name);
        displayAndRepaint();
    }
}
