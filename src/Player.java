import java.util.ArrayList;
import java.util.List;

public class Player {
    private int score;

    private final String name;

    public IdentityCard identityCard; //TODO Check if necessary

    public List<CardState> hand = new ArrayList<>();

    Player(final String name) {
        this.name = name;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(final int value) {
        this.score = value;
    }

    public String getName() {
        return this.name;
    }

    public void addCardToHand(RumourCard rumourCard) {
        this.hand.add(new CardState(rumourCard));
    }

    public void removeCardFromHand(RumourCard rumourCard) {
        this.hand.removeIf(cardState -> cardState.rumourCard == rumourCard);
    }

    public void accuse(final Player accusedPlayer) {
    }

    public void revealRumorCard(final RumourCard cardToReveal) {
    }

    public void revealIdentity() {
    }

    public void play() {
    }

}
