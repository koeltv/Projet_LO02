package com.model.player;

import com.controller.GameController;
import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The type Agressive.
 */
public class Agressive implements Strategy {
    /**
     * The linked AI.
     */
    private final AI ai;

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
        this.numberOfAccusationPerPlayer = new HashMap<>();
        this.ai = ai;
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
            return possibleActions.get(GameController.randomInInterval(possibleActions.size() - 1));
        }
    }

    @Override
    public void selectIdentity() {
        IdentityCard identityCard = RoundController.getRoundController().getPlayerIdentityCard(ai);
        identityCard.setWitch(GameController.randomInInterval(1) > 0);
    }

    @Override
    public Player selectPlayer(List<Player> players) {
        if (numberOfAccusationPerPlayer.size() > 0) {
            ArrayList<Player> selectablePlayers = new ArrayList<>();
            //Add all players who accused in ascending order, using insertion sorting
            numberOfAccusationPerPlayer.keySet()
                    .stream()
                    .filter(players::contains)
                    .forEach(accuser -> IntStream.range(0, selectablePlayers.size())
                            .filter(i -> numberOfAccusationPerPlayer.get(accuser) >= numberOfAccusationPerPlayer.get(selectablePlayers.get(i)))
                            .findFirst()
                            .ifPresent(i -> selectablePlayers.add(i, accuser)));
            //Add all players who didn't accuse at the end of the list
            selectablePlayers.addAll(players
                    .stream()
                    .filter(player -> !numberOfAccusationPerPlayer.containsKey(player))
                    .collect(Collectors.toCollection(ArrayList::new))
            );
            if (selectablePlayers.size() > 0) return selectablePlayers.get(0);
        }
        return players.get(GameController.randomInInterval(players.size() - 1));
    }

    @Override
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return rumourCards.get(GameController.randomInInterval(rumourCards.size() - 1));
    }

    @Override
    public int selectCard(int listSize) {
        return GameController.randomInInterval(listSize - 1);
    }

}
