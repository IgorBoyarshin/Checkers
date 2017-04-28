package checkers.players;

import checkers.board.FunctionGetBoardRepresentation;
import checkers.util.Pair;
import checkers.util.Vector2i;

import java.util.function.Predicate;

/**
 * Describes an abstract Player. Other Player classes will be inherited from it.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public abstract class Player {

    /**
     * The side that this Player plays for.
     */
    protected final PlayerSide playerSide;

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
     * @param functionGetBoardRepresentation the function by which the Player can receive the current situation on the board.
     * @param playerCode the code of the Player in the board representation.
     * @param canCheckerEat function o determine whether the Checker can eat.
     * @param canCheckerMove function o determine whether the Checker can move.
     * @return a Pair of positions: what Checker to move and where to move it,
     * null if the Player has not come up with a move yet.
     */
    public abstract Pair<Vector2i> makeMove(
            FunctionGetBoardRepresentation functionGetBoardRepresentation,
            int playerCode,
            Predicate<Vector2i> canCheckerEat,
            Predicate<Vector2i> canCheckerMove);

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
