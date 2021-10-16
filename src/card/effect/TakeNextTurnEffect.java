package card.effect;

import card.CardName;
import game.Game;
import player.Player;

public class TakeNextTurnEffect implements Effect {
    public boolean applyEffect(final Player cardUser, final Player target) {
        Game.getGame().round.setNextPlayer(cardUser);
        return true;
    }

    public Player chooseTarget(final CardName cardName) {
        // TODO Auto-generated return
        return null;
    }

}
