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
     * Show Game Winner.
     * This function shows the game winner.
     */
    @Override
    public synchronized void showGameWinner(String name, int numberOfRound) {
        displayAndRepaint("Congratulations " + name + ", you won in " + numberOfRound + " rounds !");
    }

    /**
     * Show Round Winner.
     * This function shows the round winner.
     */
    @Override
    public void showRoundWinner(String name) {
        displayAndRepaint(name + " won this round !");
    }

    /**
     * Show Start of Round.
     * This function shows the start of a new round and its number.
     */
    @Override
    public void showStartOfRound(int numberOfRound) {
        displayAndRepaint("Start of Round " + numberOfRound);
    }

    /**
     * Show Player Identity.
     * This function shows the player's identity.
     */
    @Override
    public void showPlayerIdentity(String name, boolean witch) {
        displayAndRepaint(name + " is a " + (witch ? "witch" : "villager") + " !");
    }

    /**
     * Show Reveal Action.
     * This function shows the revelation of a player identity.
     */
    @Override
    public void showRevealAction(String name) {
        displayAndRepaint("Player " + name + " is revealing his identity !");
    }

    /**
     * Show Accuse Action.
     * This function shows the accusation of a targeted player.
     */
    @Override
    public void showAccuseAction(String name, String targetedPlayerName) {
        displayAndRepaint("Player " + name + " is accusing " + targetedPlayerName + " !");
    }

    /**
     * Show Use Card Action.
     * This function shows the card which is used by the player.
     */
    @Override
    public void showUseCardAction(String name, String chosenCardName) {
        displayAndRepaint("Player " + name + " is using " + chosenCardName + " !");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Passive Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Wait For Player Name.
     */
    @Override
    public void waitForPlayerName(int playerIndex) {
        displayAndRepaint();
    }

    /**
     * Wait For New Game.
     */
    @Override
    public void waitForNewGame() {
        displayAndRepaint();
    }

    /**
     * Wait For Player Choice.
     */
    @Override
    public void waitForPlayerChoice(List<String> playerNames) {
        displayAndRepaint();
    }

    /**
     * Wait For Card Choice.
     * 
     * @see com.model.card.RumourCard
     */
    @Override
    public void waitForCardChoice(List<RumourCard> rumourCards) {
        displayAndRepaint();
    }

    /**
     * Wait For Repartition.
     */
    @Override
    public void waitForRepartition() {
        displayAndRepaint();
    }

    /**
     * Wait For Player Identity.
     */
    @Override
    public void waitForPlayerIdentity(String name) {
        actualiseMainPlayer(name);
        displayAndRepaint();
    }

    /**
     * Wait For Action.
     * 
     * @see com.controller.PlayerAction
     */
    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
        displayAndRepaint();
    }

    /**
     * Wait For Player Switch.
     */
    @Override
    public void waitForPlayerSwitch(String name) {
        actualiseMainPlayer(null);
        displayAndRepaint();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Prompt For Action.
     * 
     * @see com.controller.PlayerAction
     */
    @Override
    public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        actualiseMainPlayer(playerName);
        displayAndRepaint();
        return super.promptForAction(playerName, possibleActions);
    }

    /**
     * Prompt For Player Switch.
     */
    @Override
    public void promptForPlayerSwitch(String name) {
        actualiseMainPlayer(null);
        displayAndRepaint();
        super.promptForPlayerSwitch(name);
    }

    /**
     * Prompt For Player Identity.
     */
    @Override
    public int promptForPlayerIdentity(String name) {
        actualiseMainPlayer(name);
        displayAndRepaint();
        return super.promptForPlayerIdentity(name);
    }

    /**
     * Show Card List.
     */
	@Override
    public void showCardList(String name, List<String> cards) {
        actualiseMainPlayer(name);
        displayAndRepaint();
    }
}
