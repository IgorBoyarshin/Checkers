package checkers.players;

import checkers.util.Pair;
import checkers.util.Vector2i;

/**
 * Describes an abstract Player. Other Player classes will be inherited from it.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public abstract class Player {

    /**
     * The side that this Player plays for.
     */
    private final PlayerSide playerSide;

    /**
     * The constructor for the class.
     *
     * @param playerSide the side that this Player plays for.
     */
    Player(PlayerSide playerSide) {
        this.playerSide = playerSide;
    }

    /**
     * Asks the Player to make a move.
     * If the Player does not know for now what move to make, return null.
     * Otherwise return a pair of two positions: what Checker to move and where to move it.
     * A Player can make several moves during his turn(separated with arbitrary amount of null-moves),
     * provided that all the moves are eating-moves.
     *
     * @return a Pair of positions: what Checker to move and where to move it,
     * null if the Player has not come up with a move yet.
     */
    public abstract Pair<Vector2i> makeMove();

    /**
     * Checks whether the Player has finished making his turn.
     *
     * @return true if the Player has finished making his turn, false otherwise.
     */
    public abstract boolean isTurnFinished();

    /**
     * Is called when the Game has seen that the Player has finished his turn.
     * The Game will now move into the TRANSITION state.
     * The default implementation is empty, override it if some actions need to be performed.
     */
    public void confirmTurnFinished() {}

    /**
     * Checks whether this Player is a human player.
     *
     * @return whether this Player is a human player.
     */
    public abstract boolean isHuman();

    /**
     * Returns the side of this Player.
     *
     * @return the side of this Player.
     */
    public PlayerSide getPlayerSide() {
        return playerSide;
    }
}
