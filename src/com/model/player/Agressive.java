package com.model.player;

import com.controller.PlayerAction;
import com.model.game.Round;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Agressive.
 * 
 * Gives all the methods related to the Agressive strategy.
 */
public class Agressive extends Strategy {
    /**
     * The Number of accusation per player.
     * 
     * @see com.model.player.Player
     */
    public final HashMap<Player, Integer> numberOfAccusationPerPlayer;

    /**
     * Instantiates a new Agressive strategy.
     *
     * @param ai the linked AI
     * @see com.model.player.AI
     */
    public Agressive(AI ai) {
        super(ai);
        this.numberOfAccusationPerPlayer = new HashMap<>();
    }

    @Override
    public PlayerAction use(List<PlayerAction> possibleActions) {
        //Add to the number of accusation if the AI was accused
        if (possibleActions.contains(PlayerAction.REVEAL_IDENTITY)) {
            numberOfAccusationPerPlayer.merge(Round.getCurrentPlayer(), 0, (existingValue, unused) -> existingValue++);
        }
        //Choose next action, priority to accuse, otherwise use a card and if not possible choose a random action
        if (possibleActions.contains(PlayerAction.ACCUSE)) {
            return PlayerAction.ACCUSE;
        } else if (possibleActions.contains(PlayerAction.REVEAL_IDENTITY) && !Round.getInstance().getPlayerIdentityCard(ai).isWitch()) {
            return PlayerAction.REVEAL_IDENTITY;
        } else if (possibleActions.contains(PlayerAction.USE_CARD)) {
            return PlayerAction.USE_CARD;
        } else {
            return possibleActions.get(randomInInterval(possibleActions.size() - 1));
        }
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

}
