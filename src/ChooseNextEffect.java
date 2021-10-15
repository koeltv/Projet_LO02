
public class ChooseNextEffect implements Effect {
    public boolean applyEffect(final Player cardUser, final Player target) {
        if (cardUser != target) {
            Game.getGame().round.setNextPlayer(target);
            return true;
        } else
            return false;
    }

    public Player chooseTarget(final CardName cardName) { //TODO
        System.out.println("Choose the next player");
//        Player chosenPlayer;
        return null;
    }

}
