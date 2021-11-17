package com.model.player;

import com.controller.GameController;
import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Agressive implements Strategy {
    
	public final HashMap<Player, Integer> numberOfAccusationPerPlayer;
	
	public Agressive() {
		this.numberOfAccusationPerPlayer = new HashMap<>();
	}
	
	@Override
    public PlayerAction use(AI ai, List<PlayerAction> possibleActions) { //TODO Temporary implementation, need to be developed
		if(possibleActions.contains(PlayerAction.REVEAL_IDENTITY)) {
			numberOfAccusationPerPlayer.putIfAbsent(RoundController.getCurrentPlayer(), 0);
			numberOfAccusationPerPlayer.put(RoundController.getCurrentPlayer(), 1 + numberOfAccusationPerPlayer.get(RoundController.getCurrentPlayer()));
		}
		if(possibleActions.contains(PlayerAction.ACCUSE)) {
			return PlayerAction.ACCUSE;
		} else if(ai.getSelectableCardsFromHand().size() > 0) {
			return PlayerAction.USE_CARD;
		} else {
			return PlayerAction.REVEAL_IDENTITY;
		}
    }

    @Override
    public void selectIdentity(AI ai) {
        IdentityCard identityCard = RoundController.getRoundController().getPlayerIdentityCard(ai);
        identityCard.setWitch(GameController.randomInInterval(0, 1) > 0);
    }

    @Override
    public Player selectPlayer(List<Player> players) {
		
    	Player chosenPlayer;

        int max = Collections.max(numberOfAccusationPerPlayer.values());
        List<Player> selectablePlayers = numberOfAccusationPerPlayer.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        chosenPlayer = selectablePlayers.get(0);
    	return chosenPlayer;
    }

    @Override
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return rumourCards.get(GameController.randomInInterval(0, rumourCards.size() - 1));
    }

    @Override
    public int selectCard(int listSize) {
        return GameController.randomInInterval(0, listSize - 1);
    }

}
