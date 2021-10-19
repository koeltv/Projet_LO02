package card;

import card.effect.Effect;
import game.Round;
import player.Player;

import java.util.List;

public class RumourCard {
    private final CardName cardName;

    private final List<Effect> witchEffects;

    private final List<Effect> huntEffects;

    public RumourCard(CardName name, List<Effect> witchEffects, List<Effect> huntEffects) {
        this.cardName = name;
        this.witchEffects = witchEffects;
        this.huntEffects = huntEffects;
    }

    public CardName getCardName() {
        return this.cardName;
    }

    /**
     * Apply the wanted effects of the card
     * This method check which effects of the card to use (witch? effects or hunt! effects) and apply them
     * @param cardUser - the player that used this card
     * @return Whether the card has been used successfully or not
     */
    public boolean useCard(Player cardUser) {
        return cardUser == Round.getCurrentPlayer() ? applyHuntEffects(cardUser) : applyWitchEffects(cardUser);
    }

    /**
     * Apply the witch? effects of a card
     * @param cardUser - the player that used this card
     * @return - Whether the effects have been used successfully or not
     */
    public boolean applyWitchEffects(Player cardUser) {
        for (Effect witchEffect : this.witchEffects) {
            if (!witchEffect.applyEffect(cardUser, witchEffect.chooseTarget(this.cardName))) return false;
        }
        return true;
    }

    /**
     * Apply the hunt! effects of a card
     * @param cardUser - the player that used this card
     * @return - Whether the effects have been used successfully or not
     */
    public boolean applyHuntEffects(Player cardUser) {
        for (Effect huntEffect : this.huntEffects) {
            if (!huntEffect.applyEffect(cardUser, huntEffect.chooseTarget(this.cardName))) return false;
        }
        return true;
    }

}
