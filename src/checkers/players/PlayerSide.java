package checkers.players;

/**
 * This enumeration describes possible sides for the players.
 * The are two players in each game and each one of them has his own side.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public enum PlayerSide {
    /**
     * The side of the player that is located in the bottom of the screen.
     */
    PLAYER_DOWN(-1), // Forward direction is UP

    /**
     * The side of the player that is located in the top of the screen.
     */
    PLAYER_UP(+1); // Forward direction is DOWN

    /**
     * Single step along Y axis which indicates the direction of forward movement for the Checkers of the player.
     * If forward movement is DOWN then this value is +1, otherwise it is -1.
     */
    public final int forwardDirectionY;

    /**
     * The constructor for this enumeration.
     *
     * @param forwardDirectionY a single step along Y axis.
     */
    PlayerSide(int forwardDirectionY) {
        this.forwardDirectionY = forwardDirectionY;
    }
}
