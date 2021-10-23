package com.model.player;

import com.controller.GameController;
import com.model.game.IdentityCard;
import com.controller.RoundController;

import java.util.List;

public class Defensive implements Strategy {
    public void use(AI ai) { //TODO Temporary implementation, need to be developed
        if (RoundController.getCurrentPlayer() == ai) {
            List<IdentityCard> selectablePlayers = ai.getSelectablePlayers();
            Player selectedPlayer = selectablePlayers.get(GameController.randomInInterval(0, selectablePlayers.size() - 1)).player;
            System.out.println(ai.getName() + " is accusing " + selectedPlayer.getName() + " !");
            ai.accuse(selectedPlayer);
        } else {
            System.out.println(ai.getName() + " is revealing his identity !");
            ai.revealIdentity();
        }
    }

    public void selectIdentity(AI ai) {
        ai.identityCard.setWitch(GameController.randomInInterval(0, 1) > 0);
    }

}
