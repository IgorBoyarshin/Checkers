package checkers.players;

/**
 * Created by Igorek on 10-Apr-17 at 9:18 PM.
 */
public enum PlayerSide {
    PLAYER_DOWN(-1), // Forward direction is UP
    PLAYER_UP(+1); // Forward direction is DOWN

    /**
     * Single step along Y axis which indicates the direction of forward movement.
     * If forward movement is DOWN then this value is +1, otherwise it is -1.
     */
    public final int forwardDirectionY;

    PlayerSide(int forwardDirectionY) {
        this.forwardDirectionY = forwardDirectionY;
    }
}
