package com.controller;

/**
 * The enum Game action. 
 * 
 * Action choices at the end of a game. Players can select one of those choices (constants) : RESTART_GAME, RESET_GAME, STOP.
 * 
 * @see com.controller.GameController
 */
public enum GameAction {
    
	/**
     * Restart a new game with existing players.
     */
    RESTART_GAME,
    /**
     * Restart a new game with new players.
     */
    RESET_GAME,
    /**
     * End the program.
     */
    STOP
}
