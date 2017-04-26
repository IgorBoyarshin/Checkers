package checkers.players;

import checkers.util.Pair;
import checkers.util.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a computer Player.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class ComputerPlayer extends Player {

    /**
     * Time to simulate thinking on the turn. The turn will elapse approximately that much milliseconds.
     */
    private static final long THINKING_TIME_MILLIS = 1000;

    /**
     * The time when the thinking started(in millis).
     * Note that this measurement of time is done via System.currentTImeMillis(),
     * not as elsewhere in the program.
     */
    private long thinkingTimeStartMillis;

    /**
     * Consists of a series of individual moves.
     * The first element of each Pair is the position of the Checker that must be moved.
     * This first elements of each Pair should point to the same Checker object, the only difference is the
     * position that it is currently in.
     */
    private List<Pair<Vector2i>> moves;

    /**
     * The constructor for the class.
     *
     * @param playerSide the side of this Player.
     */
    public ComputerPlayer(PlayerSide playerSide) {
        super(playerSide);

        this.thinkingTimeStartMillis = 0;
        this.moves = null;
    }

    /**
     * Asks the Player to make a move.
     * If the Player does not know for now what move to make => return null.
     * Otherwise return a pair of two positions: what Checker to move and where to move it.
     * A Player can make several moves during his turn(separated with arbitrary amount of null-moves),
     * provided that all the moves are eating-moves.
     *
     * @return a Pair of positions: what Checker to move and where to move it,
     * null if the Player has not come up with a move yet.
     */
    @Override
    public final Pair<Vector2i> makeMove() {
        if (moves == null && isTurnFinished()) {
            // first call to makeMove() => come up with a series of moves for this turn
            this.thinkingTimeStartMillis = System.currentTimeMillis();
            moves = generateMoves();
        }

        if (moves != null) {
            if (moves.size() > 0) {
                return moves.remove(0);
            } else {
                moves = null; // prepare for next turn
            }
        }

        return null;
    }

    /**
     * Checks whether the Player has finished his turn.
     * Note that in the case of ComputerPlayer the actual condition of the end of the turn is
     * (isTurnFinished() && makeMove() == null).
     * In most cases (makeMove() == null) should already be true if isTurnFinished() returns true.
     *
     * @return true if the Player has finished his turn, false otherwise.
     */
    // TODO: can perform this check in makeMove(), and here just return the moveFinished variable
    @Override
    public boolean isTurnFinished() {
        return (System.currentTimeMillis() - thinkingTimeStartMillis) >= THINKING_TIME_MILLIS;
    }

    /**
     * Checks whether this Player is a human Player.
     * Returns false, because it is a ComputerPlayer.
     *
     * @return false, because it is a ComputerPlayer.
     */
    @Override
    public boolean isHuman() {
        return false;
    }

    /**
     * Generates a series of moves for current turn.
     * Can be overridden by subclasses.
     *
     * @return a series of moves to make during current turn.
     */
    protected List<Pair<Vector2i>> generateMoves() {
        List<Pair<Vector2i>> moves = new ArrayList<>();

        // TODO

        return moves;
    }
}
