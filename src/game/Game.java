package game;

import game.board.Board;
import game.board.BoardCellColor;
import game.checker.CheckerColor;
import game.players.Player;
import javafx.scene.canvas.GraphicsContext;
import util.Vector2d;

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
            int boardSizeInCells, double boardSizeInPixels,
            Player firstPlayer, Player secondPlayer,
            CheckerColor firstPlayerCheckerColor, CheckerColor secondPlayerCheckerColor,
            BoardCellColor boardCellColor) {

        this.gc = gc;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayerCheckerColor = firstPlayerCheckerColor;
        this.secondPlayerCheckerColor = secondPlayerCheckerColor;

        this.board = new Board(boardSizeInCells, boardSizeInPixels,
                firstPlayerCheckerColor, secondPlayerCheckerColor, boardCellColor);

        this.currentPlayer = null;

        // The game has not started yet
        this.currentGameState = GameState.IDLE;
    }

    public void processMousePress(Vector2d position) {
        final boolean isCheckerNotSelected = board.selectChecker(position);

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
