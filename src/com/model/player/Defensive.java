package com.model.player;

import com.controller.GameController;
import com.model.game.IdentityCard;
import com.controller.RoundController;

import java.util.List;

public class Defensive implements Strategy {
    public void use(AI ai) { //TODO Temporary implementation, need to be developed
        if (RoundController.getCurrentPlayer() == ai) {
            List<IdentityCard> selectablePlayers = RoundController.roundController.getSelectablePlayers(ai);
            Player selectedPlayer = selectablePlayers.get(GameController.randomInInterval(0, selectablePlayers.size() - 1)).player;
            System.out.println(ai.getName() + " is accusing " + selectedPlayer.getName() + " !");

            RoundController.roundController.askPlayerForAction(selectedPlayer);
            ai.accuse(selectedPlayer);
        } else {
            System.out.println(ai.getName() + " is revealing his identity !");

            RoundController.roundController.numberOfNotRevealedPlayers--;
            ai.revealIdentity();
        }
    }

    public void selectIdentity(AI ai) {
        ai.identityCard.setWitch(GameController.randomInInterval(0, 1) > 0);
    }

}
