package com.controller;

import com.model.card.RumourCard;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;

import java.util.List;

/**
 * The type Ai controller.
 */
public class AIController extends PlayerController {

    /**
     * Instantiates a new AI controller.
     *
     * @param playerName the player name
     * @param view       the view
     */
    AIController(String playerName, ActiveView view) {
        super(view);
        this.player = new AI(playerName);
    }

    @Override
    public void chooseIdentity() {
        ((AI) player).selectIdentity();
    }

    @Override
    public RumourCard chooseCard(List<RumourCard> rumourCardList) {
        return ((AI) player).selectCard(rumourCardList);
    }

    @Override
    public RumourCard chooseCardBlindly(List<RumourCard> rumourCardList) {
        int index = ((AI) player).selectCard(rumourCardList.size());
        return rumourCardList.get(index);
    }

    @Override
    public Player choosePlayer(List<Player> playerList) {
        return ((AI) player).selectPlayer(playerList);
    }

    @Override
    public void askPlayerForAction(List<PlayerAction> possibleActions) {
        //Ask the player to choose his next action
        applyPlayerAction(((AI) player).play(possibleActions));
    }
}
