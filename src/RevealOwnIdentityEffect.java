
public class RevealOwnIdentityEffect implements Effect {
    public boolean applyEffect(Player cardUser, Player target) {
        cardUser.setIdentityRevealed(true);
        System.out.print("The player is a ");
        if (cardUser.isWitch()) {
            System.out.print("witch");
        } else {
            System.out.print("villager");
        }
        return true;
    }

    public Player chooseTarget(final CardName cardName) {
        return null;
    }

}
