package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;
import com.model.player.PlayerAction;

public class RevealOwnIdentityEffect extends Effect {
    @Override
    public String toString() {
        return """
                Reveal your identity.
                Witch: Player to your left takes next turn.
                Villager: Choose next player.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {

        RoundController round = RoundController.getRoundController();
        round.applyPlayerAction(cardUser, PlayerAction.REVEAL_IDENTITY);

        Player nextPlayer = null;
        if (round.getPlayerIdentityCard(cardUser).isWitch()) {
            for (int i = 0; i < round.identityCards.size(); i++) {
                if (round.identityCards.get(i).player == cardUser && i < round.identityCards.size() - 1) {
                    nextPlayer = round.identityCards.get(i + 1).player;
                    break;
                } else if (i == round.identityCards.size() - 1) {
                    nextPlayer = round.identityCards.get(0).player;
                    break;
                }
            }
        } else {
            nextPlayer = round.choosePlayer(cardUser, round.getSelectablePlayers(cardUser));
        }
        RoundController.getRoundController().setNextPlayer(nextPlayer);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return cardUser;
    }

}
