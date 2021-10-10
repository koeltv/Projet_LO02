
public class TakeNextTurnEffect implements Effect {
    public boolean applyEffect(Player cardUser, Player target) {
        Game.getGame().round.setNextPlayer(cardUser);
        return true;
    }

    public Player chooseTarget(final CardName cardName) {
        return null;
    }

}
