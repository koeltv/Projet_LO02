package card.effect;

import card.CardName;
import player.Player;

/**
 * The interface Effect.
 */
public interface Effect {
    /**
     * Apply effect.
     *
     * @param cardUser the card user
     * @param target   the target
     * @return whether the effect was successfully applied or not
     */
    boolean applyEffect(Player cardUser, Player target);

    /**
     * Choose target player.
     *
     * @param cardName the card name
     * @return the chosen player
     */
    Player chooseTarget(CardName cardName);

}
