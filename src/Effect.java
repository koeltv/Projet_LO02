
public interface Effect {
    boolean applyEffect(Player cardUser, Player target);

    Player chooseTarget(CardName cardName);

}
