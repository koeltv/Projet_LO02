import java.util.List;

public class RumourCard {
    private static int numberOfCards;

    private final CardName cardName;

    private final List<Effect> witchEffects;

    private final List<Effect> huntEffects;

    public RumourCard(CardName name, List<Effect> witchEffects, List<Effect> huntEffects) {
        this.cardName = name;
        this.witchEffects = witchEffects;
        this.huntEffects = huntEffects;
        numberOfCards++;
    }

    public CardName getCardName() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.cardName;
    }

    public static int getNumberOfCards() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return numberOfCards;
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
