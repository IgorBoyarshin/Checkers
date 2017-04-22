package checkers.game;

import checkers.CheckersSettings;
import checkers.board.Board;
import checkers.board.BoardCellColor;
import checkers.checker.Checker;
import checkers.checker.CheckerColor;
import checkers.players.Player;
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

    public Game(
            GraphicsContext gc,
            int boardSizeInCells,
            Player firstPlayer, Player secondPlayer,
            CheckerColor firstPlayerCheckerColor, CheckerColor secondPlayerCheckerColor,
            BoardCellColor boardCellColor) {

        this.gc = gc;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayerCheckerColor = firstPlayerCheckerColor;
        this.secondPlayerCheckerColor = secondPlayerCheckerColor;

        this.board = new Board(
                boardSizeInCells,
                firstPlayerCheckerColor,
                secondPlayerCheckerColor,
                boardCellColor);

        this.currentPlayer = null;

        // The game has not started yet
        this.currentGameState = GameState.IDLE;
    }

    public void processMousePress(Vector2d position) {
//        final boolean isCheckerNowSelected = board.selectChecker(position);

        if (!board.isCheckerSelected()) {
            final boolean isCheckerNowSelected = board.selectChecker(position);
        } else {
            // A checker is already selected, try to make a move now
            System.out.println(board.isMovePossibleWithSelectedChecker(position) ? "possible" : "wrong");
            board.deselectChecker();
        }


        switch (currentGameState) {
            case PLAYER_TURN:
                // Select a checker or make the move


                break;
            case IDLE:

        }
    }

    /**
     *
     */
    public void update(double secondsSinceStart) {
        board.update(secondsSinceStart);
    }

    // TODO: consider removing the argument
    public void render(double secondsSinceStart) {
        board.draw(gc);
    }

}
