package checkers.players;

import checkers.board.FunctionGetBoardRepresentation;
import checkers.util.Pair;
import checkers.util.Vector2i;

import java.util.function.Predicate;

/**
 * Describes a human Player.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class HumanPlayer extends Player {

    /**
     * The move that this Player has made.
     * It is stored in this variable and then accessed from the main game loop.
     */
    private Pair<Vector2i> move;

    /**
     * Whether this Player has finished his turn.
     */
    private boolean turnFinished;

    /**
     * The constructor for the class.
     *
     * @param playerSide the side of this Player.
     */
    public HumanPlayer(PlayerSide playerSide) {
        super(playerSide);

        this.move = null;
    }

    /**
     * Provides this HumanPlayer with a move that he must make.
     * Essentially serves as a means to make the makeMove() method return the given value.
     * This method is called when an Event during the HumanPlayer turn happens.
     *
     * @param move the move to accept.
     */
    public void acceptMove(Pair<Vector2i> move) {
        this.move = move;
    }

    /**
     * Finishes the HumanPlayer move.
     * As a human being is the most difficult thing to communicate with, we need special means to do it.
     * This method will be called from the main game loop when this Player will have finished making his move.
     * As it would be inconvenient to make the player press a button each time he has made a move, we want to
     * figure out for him most cases when he has finished. That's when this method is called.
     */
    public void forceTurnFinish() {
        this.turnFinished = true;
    }

    /**
     * Asks this Player whether he has come up with a move to make.
     * If he has come up with something, return the move as a Pair of coordinates - the position
     * of the Checker to move and where to move it.
     * If the Player hasn't come up with a move yet, return null.
     *
     * @param functionGetBoardRepresentation the function by which the Player can receive the current situation on the board.
     * @param playerCode                     the code of the Player in the board representation.
     * @return the move that the Player has come up with, null otherwise.
     */
    @Override
    public Pair<Vector2i> makeMove(
            FunctionGetBoardRepresentation functionGetBoardRepresentation,
            int playerCode,
            Predicate<Vector2i> canCheckerEat,
            Predicate<Vector2i> canCheckerMove) {
        final Pair<Vector2i> moveToMake = move;
        this.move = null; // don't make this move next time

        return moveToMake;
    }

    /**
     * Checks whether the Player has finished his turn.
     *
     * @return true if the Player has finished his turn, false otherwise.
     */
    @Override
    public boolean isTurnFinished() {
        return turnFinished;
    }

    /**
     * Is called when the Game has seen that the Player has finished his turn.
     * The Game will now move into the TRANSITION state.
     * Prepares the fields for the next turn.
     */
    @Override
    public void confirmTurnFinished() {
        turnFinished = false; // prepare for next turn
    }

    /**
     * Checks whether this Player is a human player.
     * Returns true, because it is a HumanPlayer.
     *
     * @return true, because this is a HumanPlayer.
     */
    @Override
    public boolean isHuman() {
        return true;
    }
}
