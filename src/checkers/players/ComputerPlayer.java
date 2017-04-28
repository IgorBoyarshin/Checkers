package checkers.players;

import checkers.CheckersSettings;
import checkers.board.FunctionGetBoardRepresentation;
import checkers.util.Pair;
import checkers.util.Vector2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Describes a computer Player.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class ComputerPlayer extends Player {

    /**
     * Time to simulate thinking on the turn. The turn will elapse approximately that much milliseconds.
     */
    private static final long THINKING_TIME_MILLIS = 700;

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
     * If the Player does not know for now what move to make, return null.
     * Otherwise return a pair of two positions: what Checker to move and where to move it.
     * A Player can make several moves during his turn(separated with arbitrary amount of null-moves),
     * provided that all the moves are eating-moves.
     *
     * @param functionGetBoardRepresentation the function by which the Player can receive the current situation on the board.
     * @param playerCode                     the code of the Player in the board representation.
     * @return a Pair of positions: what Checker to move and where to move it,
     * null if the Player has not come up with a move yet.
     */
    @Override
    public final Pair<Vector2i> makeMove(
            FunctionGetBoardRepresentation functionGetBoardRepresentation,
            int playerCode,
            Predicate<Vector2i> canCheckerEat,
            Predicate<Vector2i> canCheckerMove) {
        if (moves == null) {
            // first call to makeMove() => come up with a series of moves for this turn
            this.thinkingTimeStartMillis = System.currentTimeMillis();
            moves = generateMoves(functionGetBoardRepresentation, playerCode, canCheckerEat, canCheckerMove);
        }

        if (moves != null) {
            if (moves.size() > 0) {
                final long currentTime = System.currentTimeMillis();
                if ((currentTime - thinkingTimeStartMillis) >= THINKING_TIME_MILLIS) {
                    thinkingTimeStartMillis = currentTime;
                    return moves.remove(0);
                }
            } else {
//                moves = null; // prepare for next turn
            }
        }

        return null;
    }

    /**
     * Is called when the Game has seen that the Player has finished his turn.
     * The Game will now move into the TRANSITION state.
     */
    public void confirmTurnFinished() {
        moves = null; // prepare for next turn
    }

    /**
     * Checks whether the Player has finished his turn.
     * Note that in the case of ComputerPlayer the actual condition of the end of the turn is
     * (isTurnFinished() AND makeMove() == null).
     * In most cases (makeMove() == null) should already be true if isTurnFinished() returns true.
     *
     * @return true if the Player has finished his turn, false otherwise.
     */
    // TODO: can perform this check in makeMove(), and here just return the moveFinished variable
    @Override
    public boolean isTurnFinished() {
        return moves != null && moves.size() == 0;
//        return (System.currentTimeMillis() - thinkingTimeStartMillis) >= THINKING_TIME_MILLIS;
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
     * @param functionGetBoardRepresentation the function by which the Player can receive the current situation on the board.
     * @param playerCode                     the code of the Player in the board representation.
     * @param canCheckerEat function o determine whether the Checker can eat.
     * @param canCheckerMove function o determine whether the Checker can move.
     * @return a series of moves to make during current turn.
     */
    protected List<Pair<Vector2i>> generateMoves(
            FunctionGetBoardRepresentation functionGetBoardRepresentation,
            int playerCode,
            Predicate<Vector2i> canCheckerEat,
            Predicate<Vector2i> canCheckerMove) {
        List<Pair<Vector2i>> moves = new ArrayList<>();

        // Get the list of the positions of our Checkers
        // We will be modifying the board => don't make it final
        int[][] board = functionGetBoardRepresentation.getBoardRepresentationAsArray();
        List<Vector2i> checkersPositions = new ArrayList<>();
        final int boardSizeInCells = CheckersSettings.getInstance().boardSizeInCells;
        for (int column = 0; column < boardSizeInCells; column++) {
            for (int row = 0; row < boardSizeInCells; row++) {
                if (board[column][row] == playerCode || board[column][row] == -1 * playerCode) {
                    checkersPositions.add(new Vector2i(column, row));
                }
            }
        }

        // Randomize the moves
        Collections.shuffle(checkersPositions);

        // If we have to eat => do it(several times)
        // Find a Checker that can eat
        Vector2i positionOfCheckerThatEats = checkersPositions.stream()
                .filter(canCheckerEat)
                .findAny().orElse(null);

        if (positionOfCheckerThatEats != null) {
            // If there is a Checker that can eat => eat with it till the end
            boolean ate = true;
            while (ate) {
                ate = false;
                final boolean queen = board[positionOfCheckerThatEats.x][positionOfCheckerThatEats.y] < 0;

                for (int deltaX = -1; deltaX <= +1; deltaX += 2) {
                    for (int deltaY = -1; deltaY <= +1; deltaY += 2) {
                        if (!ate) {
                            final Vector2i landingPosition =
                                    findVictimInDirection(board, positionOfCheckerThatEats, playerCode, queen, deltaX, deltaY);

                            if (landingPosition != null) {
                                // So we can eat someone here
                                board[positionOfCheckerThatEats.x][positionOfCheckerThatEats.y] = 0; // we were here
                                board[landingPosition.x - deltaX][landingPosition.y - deltaY] = 0; // this one we ate
                                board[landingPosition.x][landingPosition.y]
                                        = playerCode * (queen ? (-1) : (+1)); // we are now here

                                moves.add(new Pair<>(positionOfCheckerThatEats, landingPosition));
                                positionOfCheckerThatEats = landingPosition;
                                ate = true;
                            }
                        }
                    }
                }
            }

            return moves;
        }

        // If we don't have anything to eat => find any move
        // Find a Checker that can move
        final Vector2i positionOfCheckerThatMoves = checkersPositions.stream()
                .filter(canCheckerMove)
                .findAny().orElse(null);

        if (positionOfCheckerThatMoves != null) {
            // If there is a Checker that can move => just make the damn move

            final boolean queen = board[positionOfCheckerThatMoves.x][positionOfCheckerThatMoves.y] < 0;
            for (int deltaX = -1; deltaX <= +1; deltaX += 2) {
                for (int deltaY = -1; deltaY <= +1; deltaY += 2) {
                    // Wrong movement direction for a simple Checker => skip direction
                    if (!queen && playerSide.forwardDirectionY != deltaY) {
                        continue;
                    }

                    final Vector2i newPosition = new Vector2i(
                            positionOfCheckerThatMoves.x + deltaX,
                            positionOfCheckerThatMoves.y + deltaY);

                    if (isPositionValid(newPosition)) {
                        if (board[newPosition.x][newPosition.y] == 0) {
                            // cell is empty => can move there
                            moves.add(new Pair<>(positionOfCheckerThatMoves, newPosition));
                            return moves;
                        }
                    }
                }
            }

            // In theory, this line is never reached, because the program should have returned in the cycle above
//            return moves;
        }

        // We can't make any move => skip the turn(return empty list)
        return moves;
    }

    /**
     * Finds a victim to eat in the specified direction.
     *
     * @param board      the board state to work with.
     * @param position   the position of the Checker
     * @param playerCode the code of the opponent Checkers.
     * @param queen      whether the Checker is a queen
     * @param deltaX     movement direction along X axis.
     * @param deltaY     movement direction along Y axis.
     * @return position where we land after eating the victim, null if no victim was found.
     */
    private Vector2i findVictimInDirection(
            int[][] board,
            Vector2i position,
            int playerCode,
            boolean queen,
            int deltaX,
            int deltaY) {

        if (queen) {
            Vector2i currentPosition = position.clone();
            currentPosition.x += deltaX;
            currentPosition.y += deltaY;

            // Move while there
            boolean found = false;
            while (isPositionValid(currentPosition.x + deltaX, currentPosition.y + deltaY)) {
                // So if this is our own code => can't eat our own Checker
                if (Math.abs(board[currentPosition.x][currentPosition.y]) == playerCode) {
                    break;
                }

                // != 0 => opponent code
                if (Math.abs(board[currentPosition.x][currentPosition.y]) != 0) {
                    found = true;
                    break;
                }

                // Make another step
                currentPosition.x += deltaX;
                currentPosition.y += deltaY;
            }

            if (found) {
                // landing position
                return new Vector2i(currentPosition.x + deltaX, currentPosition.y + deltaY);
            }
        } else {
            final Vector2i victimPosition = new Vector2i(position.x + deltaX, position.y + deltaY);
            final Vector2i landingPosition = new Vector2i(position.x + 2 * deltaX, position.y + 2 * deltaY);

            // If the positions of the victim and where we land after eating are valid
            if (isPositionValid(victimPosition) &&
                    isPositionValid(landingPosition)) {
                // abs() because there can be either a simple checker or a queen
                final int code = Math.abs(board[victimPosition.x][victimPosition.y]);
                if ((code != 0) && (code != playerCode) &&
                        (board[landingPosition.x][landingPosition.y]) == 0) {
                    // if there is a proper opponent and the next cell is free => we're goooood!
                    return landingPosition;
                }
            }
        }

        return null;
    }

    /**
     * Checks whether the specified position on the board is valid.
     *
     * @param position the position on the board.
     * @return whether the position on the board is valid.
     */
    private boolean isPositionValid(Vector2i position) {
        return isPositionValid(position.x, position.y);
    }

    /**
     * Checks whether the specified position on the board is valid.
     *
     * @param column the x coordinate of the position.
     * @param row    the y coordinate of the position.
     * @return whether the position on the board is valid.
     */
    private boolean isPositionValid(int column, int row) {
        final int boardSizeInCells = CheckersSettings.getInstance().boardSizeInCells;

        return (column >= 0 && row >= 0 && column < boardSizeInCells && row < boardSizeInCells);
    }
}
