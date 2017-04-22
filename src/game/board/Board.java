package game.board;

import game.checker.Checker;
import game.checker.CheckerColor;
import game.players.PlayerSide;
import javafx.scene.canvas.GraphicsContext;
import util.Vector2d;
import util.Vector2i;

/**
 * Created by Igorek on 09-Apr-17 at 8:50 AM.
 */
public class Board {

    /**
     * Amount of cells on each side
     */
    private final int boardSizeInCells;

    /**
     * Size in pixels of each side
     */
//    private final double boardSizeInPixels;

    /**
     * Cell size in pixels
     */
    private final double cellSize;

    /**
     * The color of the board cells(light and dark)
     */
    private final BoardCellColor boardCellColor;

    /**
     * The array containing all the Checkers as a grid.
     * If a cell is empty, then there is no Checker in it.
     *
     * board[column][row]
     */
    private final Checker[][] board;

    private final CheckerColor firstPlayerCheckerColor;
    private final CheckerColor secondPlayerCheckerColor;

    private Vector2i positionOfSelectedChecker;

    public Board(
            int boardSizeInCells, double boardSizeInPixels,
            CheckerColor firstPlayerCheckerColor, CheckerColor secondPlayerCheckerColor,
            BoardCellColor boardCellColor) {

        this.boardSizeInCells = boardSizeInCells;
//        this.boardSizeInPixels = boardSizeInPixels;
        this.firstPlayerCheckerColor = firstPlayerCheckerColor;
        this.secondPlayerCheckerColor = secondPlayerCheckerColor;
        this.boardCellColor = boardCellColor;

        this.cellSize = 1.0 * boardSizeInPixels / boardSizeInCells;

        this.board = new Checker[boardSizeInCells][boardSizeInCells];
        initializeBoard();

        this.positionOfSelectedChecker = null;
    }

    private void initializeBoard() {
        final int rowsOfCellsPerPlayer = boardSizeInCells / 2 - 1;

        for (int row = 0; row < boardSizeInCells; row++) {
            if (row >= rowsOfCellsPerPlayer && row <= boardSizeInCells - row) {
                continue;
            }

            for (int column = 0; column < boardSizeInCells; column++) {
                final boolean putChecker = isCheckerHere(row, column);
                final Checker checker;

                if (putChecker) {
                    final Vector2i boardPosition = new Vector2i(column, row);
                    if (row < rowsOfCellsPerPlayer) {
                        checker = new Checker(firstPlayerCheckerColor, PlayerSide.PLAYER_ONE, boardPosition, cellSize);
                    } else {
                        checker = new Checker(secondPlayerCheckerColor, PlayerSide.PLAYER_TWO, boardPosition, cellSize);
                    }
                } else {
                    checker = null;
                }

                board[column][row] = checker;
            }
        }
    }

    private boolean isCheckerHere(int row, int column) {
        return (row + column) % 2 == 1;
    }

    private boolean isDarkCellHere(int row, int column) {
        return (row + column) % 2 == 1;
    }

    public void draw(GraphicsContext gc) {
        drawBoard(gc);
        drawCheckers(gc);
    }

    private void drawBoard(GraphicsContext gc) {
        for (int row = 0; row < boardSizeInCells; row++) {
            for (int column = 0; column < boardSizeInCells; column++) {
                gc.setFill(isDarkCellHere(row, column) ? boardCellColor.darkColor : boardCellColor.lightColor);
                gc.fillRect(column * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }

    private void drawCheckers(GraphicsContext gc) {
        for (Checker[] checkersRow : board) {
            for (Checker checker : checkersRow) {
                if (checker != null) {
                    checker.draw(gc);
                }
            }
        }

//        for (int row = 0; row < boardSizeInCells; row++) {
//            for (int column = 0; column < boardSizeInCells; column++) {
//                final Checker checker = board[column][row]; // (x;y)
//                if (checker != null) {
//                    checker.draw(gc);
//                }
//            }
//        }
    }

    public void update(double secondsSinceStart) {
        for (Checker[] checkersRow : board) {
            for (Checker checker : checkersRow) {
                checker.update(secondsSinceStart);
            }
        }
    }

    /**
     * Tries to select a checker on the board.
     * If the position is valid, flips the selection of the checker at that position if it is present,
     * and removes any selection otherwise.
     * If the position is not valid, returns the previous state.
     *
     * @param position the position on the board where the selection should happen
     * @return whether any checker is selected after performing this operation
     */
    public boolean selectChecker(Vector2d position) {
        // Convert position into Vector2i
        final Vector2i checkerPosition = new Vector2i((int) (position.x / cellSize), (int) (position.y / cellSize));
//        System.out.println("Selection at: " + checkerPosition.toString());

//        try {
        final Checker checker = getChecker(checkerPosition);
        final Checker previouslySelectedChecker = getChecker(positionOfSelectedChecker);

        if (checker != null && checkerPosition.equals(positionOfSelectedChecker)) {
            // If we clicked on the selected checker
            checker.deselect();
            positionOfSelectedChecker = null;
//            final boolean isSelectedNow = checker.flipSelection();
//            positionOfSelectedChecker = (isSelectedNow) ? checkerPosition : null;
        } else {
            if (previouslySelectedChecker != null) {
                previouslySelectedChecker.deselect();
                positionOfSelectedChecker = null;
            }
            if (checker != null) {
                checker.select();
                positionOfSelectedChecker = checkerPosition;
            }
        }


//        if (previouslySelectedChecker != null) { // a checker was selected before
//            previouslySelectedChecker.deselect();
//        }
//
//        if (checker != null) { // so a checker is present
//            final boolean isSelectedNow = checker.flipSelection();
//            positionOfSelectedChecker = (isSelectedNow) ? checkerPosition : null;
//        } else { // no checker here
//
//
//            positionOfSelectedChecker = null;
//        }

//        } catch (ArrayIndexOutOfBoundsException e) {
//            // Invalid position argument => do nothing and just return the previous state
////            return isCheckerSelected();
//        }

        return isCheckerSelected();
    }

    /**
     * Returns the checker at the specified position if the position is valid and there is a checker present.
     * Return null otherwise.
     *
     * @param position the position of the desired checker.
     * @return the checker at that position if the position is valid and there is a checker present, null otherwise.
     */
    private Checker getChecker(Vector2i position) {
        try {
            return board[position.x][position.y];
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            // Invalid position argument
            return null;
        }
    }

    public boolean isCheckerSelected() {
        return positionOfSelectedChecker != null;
    }
}
