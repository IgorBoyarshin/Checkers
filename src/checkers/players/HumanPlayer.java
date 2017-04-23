package checkers.players;

import checkers.util.Pair;
import checkers.util.Vector2i;

/**
 * Created by Igorek on 10-Apr-17 at 9:22 PM.
 */
public class HumanPlayer extends Player {

    private Pair<Vector2i> move;

    private boolean turnFinished;

    public HumanPlayer(PlayerSide playerSide) {
        super(playerSide);

        this.move = null;
    }

    /**
     * Provides this HumanPlayer with a move that he must make.
     * Essentially serves as a means to make the makeMove() method return the given value.
     * This method is called when an Event during the HumanPlayer turn happens.
     */
    public void acceptMove(Pair<Vector2i> move) {
        this.move = move;
    }

    /**
     * Finishes the HumanPlayer move
     */
    // TODO: write documentation(explain when and why is used)
    public void forceTurnFinish() {
        this.turnFinished = true;
    }

    @Override
    public Pair<Vector2i> makeMove() {
        final Pair<Vector2i> moveToMake = move;
        this.move = null; // don't make this move next time

        return moveToMake;
    }

    @Override
    public boolean isTurnFinished() {
        return turnFinished;
    }

    @Override
    public void confirmTurnFinished() {
        turnFinished = false; // prepare for next turn
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
