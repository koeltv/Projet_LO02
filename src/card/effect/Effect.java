package card.effect;

import card.CardName;
import player.Player;

public interface Effect {
    boolean applyEffect(Player cardUser, Player target);

    Player chooseTarget(CardName cardName);

}
