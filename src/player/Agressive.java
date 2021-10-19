package player;

import game.Game;
import game.IdentityCard;
import game.Round;

import java.util.List;
import java.util.Scanner;

public class Agressive implements Strategy {
    public void use(AI ai) { //TODO Temporary implementation, need to be developed
        if (Round.getCurrentPlayer() == ai) {
            List<IdentityCard> selectablePlayers = ai.getSelectablePlayers();
            Player selectedPlayer = selectablePlayers.get(Game.randomInInterval(0, selectablePlayers.size() - 1)).player;
            System.out.println(ai.getName() + " is accusing " + selectedPlayer.getName() + " !");
            ai.accuse(selectedPlayer);
        } else {
            System.out.println(ai.getName() + " is revealing his identity !");
            ai.revealIdentity();
        }
    }

    public void selectIdentity(AI ai) {
        ai.identityCard.setWitch(Game.randomInInterval(0, 1) > 0);
    }

}
