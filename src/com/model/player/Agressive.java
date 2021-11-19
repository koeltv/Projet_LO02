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

public class Agressive implements Strategy {

    private final AI ai;

    public final HashMap<Player, Integer> numberOfAccusationPerPlayer;

    public Agressive(AI ai) {
        this.numberOfAccusationPerPlayer = new HashMap<>();
        this.ai = ai;
    }

    @Override
    public PlayerAction use(List<PlayerAction> possibleActions) { //TODO Temporary implementation, need to be developed
        if (possibleActions.contains(PlayerAction.REVEAL_IDENTITY)) {
            numberOfAccusationPerPlayer.putIfAbsent(RoundController.getCurrentPlayer(), 0);
            numberOfAccusationPerPlayer.put(RoundController.getCurrentPlayer(), 1 + numberOfAccusationPerPlayer.get(RoundController.getCurrentPlayer()));
        }
        if (possibleActions.contains(PlayerAction.ACCUSE)) {
            return PlayerAction.ACCUSE;
        } else if (ai.getSelectableCardsFromHand().size() > 0) {
            return PlayerAction.USE_CARD;
        } else {
            return PlayerAction.REVEAL_IDENTITY;
        }
    }

    @Override
    public void selectIdentity() {
        IdentityCard identityCard = RoundController.getRoundController().getPlayerIdentityCard(ai);
        identityCard.setWitch(GameController.randomInInterval(0, 1) > 0);
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
            return selectablePlayers.get(0);
        } else {
            return players.get(GameController.randomInInterval(0, players.size() - 1));
        }
    }

    @Override
    public RumourCard selectCard(List<RumourCard> rumourCards) { // TODO: 18/11/2021 Fix AI getting stuck on unusable card
        return rumourCards.get(GameController.randomInInterval(0, rumourCards.size() - 1));
    }

    @Override
    public int selectCard(int listSize) {
        return GameController.randomInInterval(0, listSize - 1);
    }

}
