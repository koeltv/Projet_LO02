package com.view;

import com.controller.PlayerAction;
import com.model.card.RumourCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Views.
 * 
 * Gives all the methods related to the views. Contain a list of passive views and 1 active view. Made to be able to handle more than 1 view at a time.
 */
public class Views extends Frame implements ActiveView, PassiveView, Runnable {

    /**
     * The Passive Views.
     *
     * @see com.view.PassiveView
     */
    private final List<PassiveView> views = new ArrayList<>();

    /**
     * The Active view.
     *
     * @see com.view.ActiveView
     */
    private ActiveView activeView;

    /**
     * Instantiates a new Views.
     *
     * @param activeView the active view to start with
     * @see com.view.ActiveView
     */
    public Views(ActiveView activeView) {
        this.activeView = activeView;

        //Used to enable the ability to switch the active view dynamically
        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Add view.
     *
     * @param view the view
     * @see com.view.PassiveView
     */
    public synchronized void addView(PassiveView view) {
        views.add(view);
    }

    /**
     * Switch active view.
     *
     * @param view the view
     * @see com.view.ActiveView
     * @see com.view.PassiveView
     */
    public synchronized void switchActiveView(ActiveView view) {
        if (activeView instanceof PassiveView) views.add((PassiveView) activeView);
        if (views.contains((PassiveView) view)) views.remove(view);

        activeView = view;
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public synchronized void showGameWinner(String name, int numberOfRound) {
        views.forEach(view -> view.showGameWinner(name, numberOfRound));
        activeView.showGameWinner(name, numberOfRound);
    }

    @Override
    public synchronized void showRoundWinner(String name) {
        views.forEach(view -> view.showRoundWinner(name));
        activeView.showRoundWinner(name);
    }

    @Override
    public synchronized void showStartOfRound(int numberOfRound) {
        views.forEach(view -> view.showStartOfRound(numberOfRound));
        activeView.showStartOfRound(numberOfRound);
    }

    @Override
    public synchronized void showPlayerIdentity(String name, boolean witch) {
        views.forEach(view -> view.showPlayerIdentity(name, witch));
        activeView.showPlayerIdentity(name, witch);
    }

    @Override
    public synchronized void showRevealAction(String name) {
        views.forEach(view -> view.showRevealAction(name));
        activeView.showRevealAction(name);
    }

    @Override
    public synchronized void showAccuseAction(String name, String targetedPlayerName) {
        views.forEach(view -> view.showAccuseAction(name, targetedPlayerName));
        activeView.showAccuseAction(name, targetedPlayerName);
    }

    @Override
    public synchronized void showUseCardAction(String name, String chosenCardName) {
        views.forEach(view -> view.showUseCardAction(name, chosenCardName));
        activeView.showUseCardAction(name, chosenCardName);
    }

    @Override
    public void showCardList(String name, List<String> cards) {
        views.forEach(passiveView -> passiveView.showCardList(name, cards));
        activeView.showCardList(name, cards);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Active Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public synchronized String promptForPlayerName(int playerIndex) {
        views.forEach(view -> view.waitForPlayerName(playerIndex));
        return activeView.promptForPlayerName(playerIndex);
    }

    @Override
    public synchronized String promptForNewGame() {
        views.forEach(PassiveView::waitForNewGame);
        return activeView.promptForNewGame();
    }

    @Override
    public synchronized int promptForPlayerChoice(String playerName, List<String> playerNames) {
        views.forEach(passiveView -> passiveView.waitForPlayerChoice(playerNames));
        return activeView.promptForPlayerChoice(playerName, playerNames);
    }

    @Override
    public synchronized int promptForCardChoice(String playerName, List<RumourCard> rumourCards) {
        views.forEach(passiveView -> passiveView.waitForCardChoice(rumourCards));
        return activeView.promptForCardChoice(playerName, rumourCards);
    }

    @Override
    public int promptForCardChoice(String playerName, int listSize) {
        views.forEach(passiveView -> passiveView.waitForCardChoice(null));
        return activeView.promptForCardChoice(playerName, listSize);
    }

    @Override
    public synchronized int[] promptForRepartition() {
        views.forEach(PassiveView::waitForRepartition);
        return activeView.promptForRepartition();
    }

    @Override
    public synchronized int promptForPlayerIdentity(String name) {
        views.forEach(passiveView -> passiveView.waitForPlayerIdentity(name));
        return activeView.promptForPlayerIdentity(name);
    }

    @Override
    public synchronized PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
        views.forEach(passiveView -> passiveView.waitForAction(playerName, possibleActions));
        return activeView.promptForAction(playerName, possibleActions);
    }

    @Override
    public synchronized void promptForPlayerSwitch(String name) {
        views.forEach(passiveView -> passiveView.waitForPlayerSwitch(name));
        activeView.promptForPlayerSwitch(name);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Runnable Method
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void run() {
        while (true) {
            if (views != null && views.size() > 0) {
                List<ActiveView> activeViews = views
                        .stream()
                        .filter(passiveView -> passiveView instanceof ActiveView)
                        .map(passiveView -> (ActiveView) passiveView)
                        .toList();

                String[] viewsToString = activeViews.stream().map(View::toString).toArray(String[]::new);

                int index = JOptionPane.showOptionDialog(
                        this,
                        "Please choose which view do you want as the main view", "Main View",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null,
                        viewsToString, viewsToString[0]
                );
                if (index == JOptionPane.CLOSED_OPTION) System.exit(0);
                switchActiveView(activeViews.get(index));
            } else {
                synchronized (this) {
                    try {
                        wait(500);
                    } catch (InterruptedException ignored) {
                        //Ignored right now, could be used later
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Passive Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void waitForPlayerName(int playerIndex) {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForPlayerName(playerIndex);
        views.forEach(passiveView -> passiveView.waitForPlayerName(playerIndex));
    }

    @Override
    public void waitForNewGame() {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForNewGame();
        views.forEach(PassiveView::waitForNewGame);
    }

    @Override
    public void waitForPlayerChoice(List<String> playerNames) {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForPlayerChoice(playerNames);
        views.forEach(passiveView -> passiveView.waitForPlayerChoice(playerNames));
    }

    @Override
    public void waitForCardChoice(List<RumourCard> rumourCards) {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForCardChoice(rumourCards);
        views.forEach(passiveView -> passiveView.waitForCardChoice(rumourCards));
    }

    @Override
    public void waitForRepartition() {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForRepartition();
        views.forEach(PassiveView::waitForRepartition);
    }

    @Override
    public void waitForPlayerIdentity(String name) {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForPlayerIdentity(name);
        views.forEach(passiveView -> passiveView.waitForPlayerIdentity(name));
    }

    @Override
    public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForAction(playerName, possibleActions);
        views.forEach(passiveView -> passiveView.waitForAction(playerName, possibleActions));
    }

    @Override
    public void waitForPlayerSwitch(String name) {
        if (activeView instanceof PassiveView passiveView) passiveView.waitForPlayerSwitch(name);
        views.forEach(passiveView -> passiveView.waitForPlayerSwitch(name));
    }
}
