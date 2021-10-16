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

    public boolean useCard(Player cardUser) {
        return cardUser == Round.getCurrentPlayer() ? applyHuntEffects(cardUser) : applyWitchEffects(cardUser);
    }

    public boolean applyWitchEffects(Player cardUser) {
        for (Effect witchEffect : this.witchEffects) {
            if (!witchEffect.applyEffect(cardUser, witchEffect.chooseTarget(this.cardName))) return false;
        }
        return true;
    }

    public boolean applyHuntEffects(Player cardUser) {
        for (Effect huntEffect : this.huntEffects) {
            if (!huntEffect.applyEffect(cardUser, huntEffect.chooseTarget(this.cardName))) return false;
        }
        return true;
    }

}
