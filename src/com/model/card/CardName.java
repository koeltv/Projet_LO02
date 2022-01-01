package com.model.card;

/**
 * The enum Card Name.
 * 
 * Names of the different selectable cards in the game : ANGRY_MOB, THE_INQUISITION, POINTED_HAT, HOOKED_NOSE, BROOMSTICK, WART, DUCKING_STOOL, CAULDRON, EVIL_EYE, TOAD, BLACK_CAT, PET_NEWT.
 * 
 * @see com.model.card.Deck
 */
public enum CardName {
    
	/**
     * Angry mob card name.
     */
    ANGRY_MOB,
    /**
     * The inquisition card name.
     */
    THE_INQUISITION,
    /**
     * Pointed hat card name.
     */
    POINTED_HAT,
    /**
     * Hooked nose card name.
     */
    HOOKED_NOSE,
    /**
     * Broomstick card name.
     */
    BROOMSTICK,
    /**
     * Wart card name.
     */
    WART,
    /**
     * Ducking stool card name.
     */
    DUCKING_STOOL,
    /**
     * Cauldron card name.
     */
    CAULDRON,
    /**
     * Evil eye card name.
     */
    EVIL_EYE,
    /**
     * Toad card name.
     */
    TOAD,
    /**
     * Black cat card name.
     */
    BLACK_CAT,
    /**
     * Pet newt card name.
     */
    PET_NEWT;

    @Override
    public String toString() {
        char[] charArray = super.toString().replace("_", " ").toCharArray();
        for (int i = 1; i < charArray.length; i++) {
            if (charArray[i - 1] != ' ') charArray[i] = Character.toLowerCase(charArray[i]);
        }
        return new String(charArray);
    }
}
