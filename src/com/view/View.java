package com.view;

import java.util.List;

/**
 * The interface View.
 * 
 * Gives all the methods related to the view. A view is a way to interact with users.
 */
public interface View {
    
	/**
     * Show game winner.
     *
     * @param name          the name of the winner
     * @param numberOfRound the number of round
     */
    void showGameWinner(String name, int numberOfRound);

    /**
     * Show round winner.
     *
     * @param name the name
     */
    void showRoundWinner(String name);

    /**
     * Show start of round.
     *
     * @param numberOfRound the number of the round
     */
    void showStartOfRound(int numberOfRound);

    /**
     * Show player identity.
     *
     * @param name  the name
     * @param witch the identity
     */
    void showPlayerIdentity(String name, boolean witch);

    /**
     * Show player action (revealing identity).
     *
     * @param name the player name
     */
    void showRevealAction(String name);

    /**
     * Show player action (accusing).
     *
     * @param name               the player name
     * @param targetedPlayerName the targeted player name
     */
    void showAccuseAction(String name, String targetedPlayerName);

    /**
     * Show player action (using card).
     *
     * @param name           the player name
     * @param chosenCardName the chosen card name
     */
    void showUseCardAction(String name, String chosenCardName);

    /**
     * Show card list.
     *
     * @param name  the name of the player
     * @param cards the cards description
     */
    void showCardList(String name, List<String> cards);
}
