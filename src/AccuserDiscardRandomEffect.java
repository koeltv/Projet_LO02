
public class AccuserDiscardRandomEffect implements Effect {
    public boolean applyEffect(Player cardUser, Player target) {
        if (target.hand.size() > 0) {
            RumourCard chosenCard = target.hand.get(Game.randomInInterval(0, target.hand.size())).rumourCard;
            Round.getCurrentPlayer().removeCardFromHand(chosenCard);
            Game.getGame().round.discardPile.add(chosenCard);
            return true;
        } else
            return false;
    }

    public Player chooseTarget(CardName cardName) {
        return Round.getCurrentPlayer();
    }

}
