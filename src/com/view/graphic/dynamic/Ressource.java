package com.view.graphic.dynamic;

/**
 * The enum Ressource.
 * 
 * Gives all the methods related to resources of this game (backgrounds, graphical elements...)
 */
public enum Ressource {
	
    /**
     * The Background.
     */
    TABLETOP("Tabletop.jpg"),
    /**
     * An empty card front.
     */
    EMPTY_CARD_FRONT("CardFrontEmpty.jpg"),
    /**
     * The Card back.
     */
    CARD_BACK("CardBack.jpg"),
    /**
     * The Identity card when the player isn't revealed.
     */
    IDENTITY_CARD("IdentityCard.jpg"),
    /**
     * The Identity card when the player is revealed as Villager.
     */
    REVEALED_VILLAGER("RevealedVillager.jpg");

    /**
     * The Path.
     */
    final String fileName;

    /**
     * Instantiates a new Ressource.
     *
     * @param fileName the ressource filename
     */
    Ressource(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
