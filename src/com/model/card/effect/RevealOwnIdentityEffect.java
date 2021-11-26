package com.model.card.effect;

import com.controller.PlayerAction;
import com.controller.RoundController;
import com.model.card.CardName;
import com.model.game.IdentityCard;
import com.model.game.Round;
import com.model.player.Player;

import java.util.List;

/**
 * The type Reveal own identity effect.
 */
public class RevealOwnIdentityEffect extends TurnEffect {
    @Override
    public String toString() {
        return """
                Reveal your identity.
                Witch: Player to your left takes next turn.
                Villager: Choose next player.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        Round round = Round.getInstance();

        Player nextPlayer = null;
        if (round.getPlayerIdentityCard(cardUser).isWitch()) {
            List<IdentityCard> identityCards = round.getIdentityCards();
            for (int i = 0; i < identityCards.size(); i++) {
                if (identityCards.get(i).player == cardUser && i < identityCards.size() - 1) {
                    nextPlayer = identityCards.get(i + 1).player;
                    break;
                } else if (i == identityCards.size() - 1) {
                    nextPlayer = identityCards.get(0).player;
                    break;
                }
            }
        } else {
            nextPlayer = RoundController.getInstance().getPlayerController(cardUser).choosePlayer(round.getSelectablePlayers(cardUser));
        }
        RoundController.getInstance().getPlayerController(cardUser).applyPlayerAction(PlayerAction.REVEAL_IDENTITY);
        round.setNextPlayer(nextPlayer);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return cardUser;
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return true;
    }

}
