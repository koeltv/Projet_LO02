package player;

/**
 * The interface Strategy.
 */
public interface Strategy {
    /**
     * Use the strategy.
     *
     * @param ai the AI using the strategy
     */
    void use(AI ai);

    /**
     * Select identity.
     *
     * @param ai the AI using the strategy
     */
    void selectIdentity(AI ai);

}
