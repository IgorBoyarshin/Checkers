package checkers.game;

import checkers.CheckersSettings;
import checkers.board.Board;
import checkers.board.BoardCellColor;
import checkers.checker.Checker;
import checkers.checker.CheckerColor;
import checkers.players.Player;
import checkers.util.Vector2i;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;

/**
 * Created by Igorek on 09-Apr-17 at 8:49 AM.
 */
public class Game {

    private final GraphicsContext gc;

    private final Player firstPlayer;
    private final Player secondPlayer;
    private final CheckerColor firstPlayerCheckerColor;
    private final CheckerColor secondPlayerCheckerColor;

    private final Board board;
    private GameState currentGameState;

    private Player currentPlayer;

    /**
     * The time in seconds when the transition(Checker movement) started.
     * Is used to determine when to switch the GameState.
     * Transition should take exactly CheckersSettings.getInstance().movementSpeed.durationInSeconds seconds.
     */
    private double transitionTimeStart;

    public Game(
            GraphicsContext gc,
            int boardSizeInCells,
            Player playerUp, Player playerDown,
            CheckerColor firstPlayerCheckerColor, CheckerColor secondPlayerCheckerColor,
            BoardCellColor boardCellColor) {

        this.gc = gc;
        this.firstPlayer = playerUp;
        this.secondPlayer = playerDown;
        this.firstPlayerCheckerColor = firstPlayerCheckerColor;
        this.secondPlayerCheckerColor = secondPlayerCheckerColor;

        this.board = new Board(
                boardSizeInCells,
                firstPlayerCheckerColor,
                secondPlayerCheckerColor,
                boardCellColor);

        this.currentPlayer = playerDown;

        // The game has not started yet
//        this.currentGameState = GameState.PLAYER_TURN;
        this.currentGameState = GameState.IDLE;

        this.transitionTimeStart = 0.0;
    }

    public void processMousePress(Vector2d position) {
        switch (currentGameState) {
            case PLAYER_TURN:
                if (!currentPlayer.isHuman()) {
                    // Don't allow mouse events while a computer is making move
                    return;
                }

                final Vector2i newPosition = board.convertPositionToVector2i(position);

                // Select a checker or make the move
                if (!board.isCheckerSelected()) {
                    final boolean isCheckerNowSelected = board.selectChecker(newPosition);
                } else {
                    // A checker is already selected, try to make a move now
                    final boolean moveValid = board.isMovePossibleWithSelectedChecker(newPosition);
//                    System.out.println(moveValid ? "possible" : "wrong");

                    if (moveValid) {
                        // Move
                        board.moveSelectedChecker(newPosition);

                        // Switch current GameState
//                        final double ONE_BILLION = 1000000000.0;
//                        transitionTimeStart = System.nanoTime() / ONE_BILLION;
                        transitionTimeStart = 0.0; // TODO: consider not needed(is set in update())
                        this.currentGameState = GameState.TRANSITION;
                    } else {
                        board.deselectChecker();
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


                break;
            case TRANSITION:
                if (transitionTimeStart == 0.0) { // We just got here
                    transitionTimeStart = secondsSinceStart;
                } else {
                    // TODO: mb speed up by not accessing the constant each frame
                    final double duration = CheckersSettings.getInstance().movementSpeed.durationInSeconds;
                    if (secondsSinceStart - transitionTimeStart > duration) {
                        // Transition has ended => switch the GameState
                        this.currentGameState = GameState.PLAYER_TURN;
                        this.transitionTimeStart = 0.0;
                    }
                }
                break;
            case IDLE:
                currentGameState = GameState.PLAYER_TURN;
                break;
            default:
        }

        board.update(secondsSinceStart);
    }

    // TODO: consider removing the argument
    public void render(double secondsSinceStart) {
        board.draw(gc);
    }

}
