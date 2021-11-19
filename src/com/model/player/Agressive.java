package com.model.player;

import com.controller.PlayerAction;
import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Agressive.
 */
public class Agressive extends Strategy {
    /**
     * The Number of accusation per player.
     */
    public final HashMap<Player, Integer> numberOfAccusationPerPlayer;

    /**
     * Instantiates a new Agressive.
     *
     * @param ai the linked AI
     */
    public Agressive(AI ai) {
        super(ai);
        this.numberOfAccusationPerPlayer = new HashMap<>();
    }

    @Override
    public PlayerAction use(List<PlayerAction> possibleActions) { //TODO Temporary implementation, need to be developed
        //Add to the number of accusation if the AI was accused
        if (possibleActions.contains(PlayerAction.REVEAL_IDENTITY)) {
            numberOfAccusationPerPlayer.putIfAbsent(RoundController.getCurrentPlayer(), 0);
            numberOfAccusationPerPlayer.put(RoundController.getCurrentPlayer(), 1 + numberOfAccusationPerPlayer.get(RoundController.getCurrentPlayer()));
        }
        //Choose next action, priority to accuse, otherwise use a card and if not possible choose a random action
        if (possibleActions.contains(PlayerAction.ACCUSE)) {
            return PlayerAction.ACCUSE;
        } else if (possibleActions.contains(PlayerAction.USE_CARD)) {
            return PlayerAction.USE_CARD;
        } else {
            return possibleActions.get(randomInInterval(possibleActions.size() - 1));
        }
    }

    @Override
    public void selectIdentity() {
        IdentityCard identityCard = RoundController.getInstance().getPlayerIdentityCard(ai);
        identityCard.setWitch(randomInInterval(1) > 0);
    }

    @Override
    public Player selectPlayer(List<Player> players) {
        if (numberOfAccusationPerPlayer.size() > 0) {
            //Select the player who accused the most
            Player player = numberOfAccusationPerPlayer.keySet().stream()
                    .filter(players::contains)
                    .max(Comparator.comparing(numberOfAccusationPerPlayer::get))
                    .orElse(null);
            if (player != null) return player;
        }
        return players.get(randomInInterval(players.size() - 1));
    }

    @Override
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return rumourCards.get(randomInInterval(rumourCards.size() - 1));
    }

    @Override
    public int selectCard(int listSize) {
        return randomInInterval(listSize - 1);
    }

}
