
public class RumourCard {
    private static int numberOfCards;

    public CardName cardName;

    public void applyHuntEffect() {
    }

    public void applyWitchEffect() {
    }

    public CardName getCardName() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.cardName;
    }

    public RumourCard(final CardName name) {
        this.cardName = name;
        numberOfCards++;
    }

    public static int getNumberOfCards() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return numberOfCards;
    }

}
