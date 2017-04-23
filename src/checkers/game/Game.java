package checkers.game;

import checkers.CheckersSettings;
import checkers.board.Board;
import checkers.board.BoardCellColor;
import checkers.checker.CheckerColor;
import checkers.players.HumanPlayer;
import checkers.players.Player;
import checkers.util.Pair;
import checkers.util.Vector2i;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;

/**
 * Created by Igorek on 09-Apr-17 at 8:49 AM.
 */
public class Game {

    private final GraphicsContext gc;

    private final Player playerUp;
    private final Player playerDown;
    private final CheckerColor playerUpCheckerColor;
    private final CheckerColor playerDownCheckerColor;

    private final Board board;
    private GameState currentGameState;

    private Player currentPlayer;

    /**
     * The time in seconds when the transition(Checker movement) started.
     * Is used to determine when to switch the GameState.
     * Transition should take exactly CheckersSettings.getInstance().movementSpeed.durationInSeconds seconds.
     */
    private double transitionTimeStart;

    private boolean playerHasMoved;
    private boolean playerEatsThisTurn;
    private boolean haveCheckedAbilityToMoveThisTurn;

    public Game(
            GraphicsContext gc,
            int boardSizeInCells,
            Player playerUp, Player playerDown,
            CheckerColor playerUpCheckerColor, CheckerColor playerDownCheckerColor,
            BoardCellColor boardCellColor) {

        this.gc = gc;
        this.playerUp = playerUp;
        this.playerDown = playerDown;
        this.playerUpCheckerColor = playerUpCheckerColor;
        this.playerDownCheckerColor = playerDownCheckerColor;

        this.board = new Board(
                boardSizeInCells,
                playerUpCheckerColor,
                playerDownCheckerColor,
                boardCellColor);

        this.currentPlayer = playerDown;

        // The game has not started yet
        this.currentGameState = GameState.IDLE;

        this.transitionTimeStart = 0.0;

        this.playerHasMoved = false;
        this.playerEatsThisTurn = false;
        this.haveCheckedAbilityToMoveThisTurn = false;
    }

    public void processMousePress(Vector2d position) {
        switch (currentGameState) {
            case PLAYER_TURN:
                if (currentPlayer.isHuman()) {
                    HumanPlayer humanPlayer = (HumanPlayer) currentPlayer;
                    final Vector2i newPosition = board.convertPositionToVector2i(position);

                    // Select a checker or make the move
                    if (!board.isCheckerSelected()) {
                        final boolean isCheckerNowSelected =
                                board.selectChecker(currentPlayer.getPlayerSide(), newPosition);
                    } else {
                        // A checker is already selected, try to make a move now
                        final boolean moveValid = board.isMovePossibleWithSelectedChecker(newPosition);
                        if (moveValid) {
                            // Move
//                            board.moveSelectedChecker(newPosition);
                            humanPlayer.acceptMove(new Pair<>(board.getPositionOfSelectedChecker(), newPosition));
                        }

                        board.deselectChecker(); // will get selected at the update() method
                    }
                }
                break;
            case IDLE:

                break;
            case TRANSITION:

                break;
            default:
        }
    }

    /**
     *
     */
    public void update(double secondsSinceStart) {
        switch (currentGameState) {
            case PLAYER_TURN:
                final Pair<Vector2i> move = currentPlayer.makeMove();
                if (move == null) {
                    if (currentPlayer.isTurnFinished()) {
                        // transition
                        currentPlayer.confirmTurnFinished();
                        this.currentGameState = GameState.TRANSITION;

                        // Prepare for next turn
                        swapPlayers();
                        this.playerHasMoved = false;
                        this.playerEatsThisTurn = false;
                        this.haveCheckedAbilityToMoveThisTurn = false;
                    } else { // Player hasn't finished his turn yet
                        if (currentPlayer.isHuman()) {
                            final HumanPlayer humanPlayer = (HumanPlayer) currentPlayer;
                            // A HumanPlayer can't finish his turn on his own except for using a button.

                            if (playerHasMoved) {
                                // There are three possible scenarios here:
                                // - just move. [already done that, so just finish the turn] {1}
                                // - eat, eat, eat, nothing to eat. [finish when nothing left ot eat] {2}
                                // - eat, eat, EndTurnButton press. [eating not mandatory => allow to finish] {3}

                                if (!playerEatsThisTurn) { // scenario {1}
                                    // Not allowed to make moves anymore => finish
                                    humanPlayer.forceTurnFinish();
                                } else { // player already ate this turn
                                    if (board.canPlayerEat(currentPlayer.getPlayerSide())) { // scenario {3}
                                        if (CheckersSettings.getInstance().isEatingMandatory) {
                                            // Wait for Player to make his move
                                        } else {
                                            // Wait for Player to press the EndTurn button
                                        }
                                    } else { // can't eat now. scenario {2}
                                        // The Player has already eaten but has nothing to eat anymore => finish
                                        humanPlayer.forceTurnFinish();
                                    }
                                }
                            } else { // player hasn't moved yet
                                // Player doesn't make a move => maybe he can't => check it
                                if (!haveCheckedAbilityToMoveThisTurn) {
                                    haveCheckedAbilityToMoveThisTurn = true;
                                    final boolean ableToMoveThisTurn = board.canPlayerMakeAnyMove(currentPlayer.getPlayerSide());

                                    if (!ableToMoveThisTurn) {
                                        // TODO: maybe notify the user somehow that he can't move
                                        humanPlayer.forceTurnFinish();
                                    }
                                }

                                // otherwise nothing, he's just thinking, give him some time, DAMN IT!!!
                            }
                        } else { // Player is a ComputerPlayer
                            // A ComputerPlayer can figure out everything for himself and can finish his turn on his own
                            // (we will even believe him when he hasn't made a move but tells us that he has finished).

                            // So we don't need to do anything, just wait for Computer to inform us that he's finished.
                        }
                    }
                } else { // move != null
                    // Make the actual move
                    board.selectChecker(currentPlayer.getPlayerSide(), move.a);
                    final boolean ateSomeone = board.moveSelectedChecker(move.b);

                    playerHasMoved = true;
                    playerEatsThisTurn = playerEatsThisTurn || ateSomeone;
                }

                break;
            case TRANSITION:
                // Just waits for the movement to end, then switches back to PLAYER_TURN

                if (transitionTimeStart == 0.0) { // We just got here
                    transitionTimeStart = secondsSinceStart;
                } else {
                    // TODO: mb speed up by not accessing the constant each frame
                    final double duration = CheckersSettings.getInstance().movementSpeed.durationInSeconds;
                    if (secondsSinceStart - transitionTimeStart > duration) {
                        // Transition has ended => switch the GameState
                        this.currentGameState = GameState.PLAYER_TURN;
                        this.transitionTimeStart = 0.0; // prepare for next transition

                        System.out.println(currentPlayer.getPlayerSide().toString() + " move");
                    }
                }
                break;
            case IDLE:
                currentGameState = GameState.PLAYER_TURN;
                System.out.println(currentPlayer.getPlayerSide().toString() + " move");
                break;
            default:
        }

        board.update(secondsSinceStart);
    }

    // TODO: consider removing the argument
    public void render(double secondsSinceStart) {
        board.draw(gc);
    }

    private void swapPlayers() {
        currentPlayer = (currentPlayer.equals(playerDown)) ? playerUp : playerDown;
    }

}
