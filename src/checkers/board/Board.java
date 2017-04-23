package checkers.board;

import checkers.checker.Checker;
import checkers.checker.CheckerColor;
import checkers.CheckersSettings;
import checkers.players.PlayerSide;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;
import checkers.util.Vector2i;

/**
 * Created by Igorek on 09-Apr-17 at 8:50 AM.
 */
public class Board {

    /**
     * Amount of cells on each side
     */
    private final int boardSizeInCells;

    /**
     * The color of the board cells(light and dark)
     */
    private final BoardCellColor boardCellColor;

    /**
     * The array containing all the Checkers as a grid.
     * If a cell is empty, then there is no Checker in it.
     * <p>
     * board[column][row]
     */
    private final Checker[][] board;

    private final CheckerColor firstPlayerCheckerColor;
    private final CheckerColor secondPlayerCheckerColor;

    private Vector2i positionOfSelectedChecker;

    private Checker checkerToDrawOnTop;

    // TODO: consider having only positionOfDyingChecker variable
    private Checker dyingChecker;

    private Vector2i positionOfDyingChecker;

    public Board(
            int boardSizeInCells,
            CheckerColor firstPlayerCheckerColor, CheckerColor secondPlayerCheckerColor,
            BoardCellColor boardCellColor) {

        this.boardSizeInCells = boardSizeInCells;
//        this.boardSizeInPixels = boardSizeInPixels;
        this.firstPlayerCheckerColor = firstPlayerCheckerColor;
        this.secondPlayerCheckerColor = secondPlayerCheckerColor;
        this.boardCellColor = boardCellColor;

        this.board = new Checker[boardSizeInCells][boardSizeInCells];
        initializeBoard();

        this.positionOfSelectedChecker = null;
        this.checkerToDrawOnTop = null;
        this.dyingChecker = null;
        this.positionOfDyingChecker = null;
    }

    private void initializeBoard() {
        if (true) {
            initializeBoardFromArray(new int[][]
                    {
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 0},
                            {0, 0, 0, -2, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 1, 0, 0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 2, 0, 0, 0},
                            {0, 0, 0, 1, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0}
                    });

            return;
        }

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
                        checker = new Checker(firstPlayerCheckerColor, PlayerSide.PLAYER_UP, boardPosition);
                    } else {
                        checker = new Checker(secondPlayerCheckerColor, PlayerSide.PLAYER_DOWN, boardPosition);
                    }
                } else {
                    checker = null;
                }

                board[column][row] = checker;
            }
        }
    }

    /**
     * Initializes the board using the given array.
     * 0 == no Checker.
     * 1 == PLAYER_UP Checker
     * -1 == PLAYER_UP Checker queen
     * 2 == PLAYER_DOWN Checker
     * -2 == PLAYER_DOWN Checker queen
     */
    private void initializeBoardFromArray(int[][] array) {
        for (int row = 0; row < boardSizeInCells; row++) {
            for (int column = 0; column < boardSizeInCells; column++) {
                final Vector2i boardPosition = new Vector2i(column, row);

                final Checker checker;
                switch (array[row][column]) {
                    case 1: // PLAYER_UP Checker
                        checker = new Checker(firstPlayerCheckerColor, PlayerSide.PLAYER_UP, boardPosition);
                        break;
                    case -1: // PLAYER_UP Checker queen
                        checker = new Checker(firstPlayerCheckerColor, PlayerSide.PLAYER_UP, boardPosition);
                        checker.makeQueen();
                        break;
                    case 2: // PLAYER_DOWN Checker
                        checker = new Checker(secondPlayerCheckerColor, PlayerSide.PLAYER_DOWN, boardPosition);
                        break;
                    case -2: // PLAYER_DOWN Checker queen
                        checker = new Checker(secondPlayerCheckerColor, PlayerSide.PLAYER_DOWN, boardPosition);
                        checker.makeQueen();
                        break;
                    case 0:
                    default:
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
        final double cellSize = CheckersSettings.getInstance().cellSize;
        for (int row = 0; row < boardSizeInCells; row++) {
            for (int column = 0; column < boardSizeInCells; column++) {
                gc.setFill(isDarkCellHere(row, column) ? boardCellColor.darkColor : boardCellColor.lightColor);
                gc.fillRect(column * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }

    private void drawCheckers(GraphicsContext gc) {
        // Draw all the Checkers
        for (Checker[] checkersRow : board) {
            for (Checker checker : checkersRow) {
                if (checker != null && !checker.equals(checkerToDrawOnTop)) {
                    checker.draw(gc);
                }
            }
        }

        // Draw the selected Checker the last, so that it is always on top(while moving)
        if (checkerToDrawOnTop != null) {
            checkerToDrawOnTop.draw(gc);
        }
    }

    public void update(double secondsSinceStart) {
        for (Checker[] checkersRow : board) {
            for (Checker checker : checkersRow) {
                if (checker != null) {
                    checker.update(secondsSinceStart);
                }
            }
        }

        if (dyingChecker != null && dyingChecker.hasDied()) {
            board[positionOfDyingChecker.x][positionOfDyingChecker.y] = null; // Remove the Checker that has died

            this.dyingChecker = null;
            this.positionOfDyingChecker = null;
        }
    }

    /**
     * Tries to select a checker on the board.
     * If the position is valid, flips the selection of the checker at that position if it is present,
     * and removes any selection otherwise.
     * If the position is not valid, returns the previous state.
     *
     * @param checkerPosition the position on the board where the selection should happen
     * @return whether any checker is selected after performing this operation
     */
    public boolean selectChecker(Vector2i checkerPosition) {
//        final double cellSize = CheckersSettings.getInstance().cellSize;
        // Convert position into Vector2i
//        final Vector2i checkerPosition = new Vector2i((int) (position.x / cellSize), (int) (position.y / cellSize));

        final Checker checker = getChecker(checkerPosition);
        final Checker previouslySelectedChecker = getChecker(positionOfSelectedChecker);

        if (checker != null && checkerPosition.equals(positionOfSelectedChecker)) {
            // If we clicked on the selected checker
            checker.deselect();
            positionOfSelectedChecker = null;
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

        return isCheckerSelected();
    }

    /**
     * Deselects any Checker that was selected before, does nothing otherwise.
     *
     * @return true if a Checker was selected before
     */
    public boolean deselectChecker() {
        final Checker checker = getChecker(positionOfSelectedChecker);
        final boolean wasSelected = checker != null;

        positionOfSelectedChecker = null;
        if (checker != null) {
            checker.deselect();
        }

        return wasSelected;
    }

    /**
     * Returns the checker at the specified position if the position is valid and there is a checker present.
     * Return null otherwise.
     *
     * @param position the position of the desired checker.
     * @return the checker at that position if the position is valid and there is a checker present, null otherwise.
     */
    // TODO: check the frequency of exception. MB better to do a simple check
    private Checker getChecker(Vector2i position) {
        try {
            return board[position.x][position.y];
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            // Invalid position argument
            return null;
        }
    }

    /**
     * Checks whether a Checker is currently selected.
     * Equivalent to (getChecker(positionOfSelectedChecker) != null).
     * The latter option is a better choice if further usage of the Checker object is desired.
     *
     * @return true if a Checker is selected, false otherwise.
     */
    public boolean isCheckerSelected() {
        return positionOfSelectedChecker != null;
    }

    /**
     * Moves the selected Checker into the specified location.
     * Deselects the Checker immediately.
     * Expects the move to be valid(does not check validness).
     *
     * @param newPosition the position into which the Checker must move.
     * @return the Checker that the selected Checker eats(if any) as the result of its move, null otherwise.
     */
    public void moveSelectedChecker(Vector2i newPosition) {
        final Checker selectedChecker = getChecker(positionOfSelectedChecker);
        if (selectedChecker == null) {
            return;
        }

        // Retrieve the Checker that the selected Checker eats(if any)
        final Vector2i delta = new Vector2i(
                newPosition.x - positionOfSelectedChecker.x,
                newPosition.y - positionOfSelectedChecker.y);
        final int moveLength = Math.abs(delta.x); // == abs(delta.y)
        final Vector2i positionOfCheckerToEat =
                new Vector2i(newPosition.x - delta.x / moveLength, newPosition.y - delta.y / moveLength);
        // If it is us => don't eat us : )
        final Checker checkerToEat =
                (moveLength == 1) ? null : board[positionOfCheckerToEat.x][positionOfCheckerToEat.y];
        if (checkerToEat != null) {
            this.positionOfDyingChecker = positionOfCheckerToEat;
            this.dyingChecker = checkerToEat;
            checkerToEat.die();
        }

        // Move the selected Checker on the board
        selectedChecker.move(newPosition);
        board[positionOfSelectedChecker.x][positionOfSelectedChecker.y] = null;
        board[newPosition.x][newPosition.y] = selectedChecker;

        // Set this Checker to be rendered on top.
        // At any time there will be a not-null reference.
        // This is OK because drawing order matters only during movement
        checkerToDrawOnTop = selectedChecker;

        // Deselect manually
        // TODO: do this with a method
        selectedChecker.deselect();
        positionOfSelectedChecker = null;

//        deselectChecker();
    }

    /**
     * Checks whether the selected checker is allowed to move into the specified cell.
     *
     * @return true if a checker is selected and it is allowed to move into the specified cell,
     * false otherwise.
     */
    // TODO: Architecture is too complex
    public boolean isMovePossibleWithSelectedChecker(Vector2i newPosition) {
        final Checker selectedChecker = getChecker(positionOfSelectedChecker);
        if (selectedChecker == null) { // if a checker is not selected
            return false;
        }

        if (getChecker(newPosition) != null) {
            // We cannot move there, there is a Checker in that cell
            return false;
        }

        final Vector2i delta = new Vector2i(
                newPosition.x - positionOfSelectedChecker.x,
                newPosition.y - positionOfSelectedChecker.y);
        if (Math.abs(delta.x) != Math.abs(delta.y)) {
            // Wrong movement
            return false;
        }

        final int moveLength = Math.abs(delta.x);
        if (moveLength == 0) {
            // Wrong movement
            return false;
        }

        final boolean checkerEats;
        final Checker potentialCheckerToEat = getChecker(
                new Vector2i(newPosition.x - delta.x / moveLength, newPosition.y - delta.y / moveLength));
        if (selectedChecker.isQueen()) {
            final int amountOfCheckersOnThePath = amountOfCheckersUntil(newPosition);
            switch (amountOfCheckersOnThePath) {
                case 0: // Just movement
                    checkerEats = false;
                    break;
                case 1: // Eating
                    // If the one Checker that we found is just before our destination => valid eating
                    checkerEats = potentialCheckerToEat != null;
                    if (!checkerEats) {
                        // Cannot move across a Checker without eating it => invalid move
                        return false;
                    }
                    break;
                default: // Wrong move
                    return false;
            }
        } else { // not queen
            switch (moveLength) {
                case 1: // Just move
                    // We can move only forward
                    if (Math.signum(delta.y) != Math.signum(selectedChecker.getPlayerSide().forwardDirectionY)) {
                        // Given movement direction is wrong => invalid move
                        return false;
                    }

                    checkerEats = false;
                    break;
                case 2: // Eating
                    if (potentialCheckerToEat == null) {
                        // No Checker to eat => invalid move
                        return false;
                    }

                    checkerEats = true;
                    break;
                default: // A simple Checker can't move more than 2 cells
                    return false;
            }
        }

        if (checkerEats) {
            if (doCheckersBelongToTheSamePlayer(selectedChecker, potentialCheckerToEat)) {
                // If the Checkers belong to the same Player => can't eat => invalid move
                return false;
            }
        }

        if (CheckersSettings.getInstance().isEatingMandatory && canSelectedCheckerEat() && !checkerEats) {
            // TODO: mb print into a Label that the move is banned due to not eating
            return false;
        }

        return true;
    }

    public boolean canSelectedCheckerEat() {
        final Checker checker = getChecker(positionOfSelectedChecker);
        if (checker != null) { // if a checker is selected
            if (checker.isQueen()) {

            } else { // not a queen

            }
        }

        return false;
    }

    /**
     * Counts the amount of Checkers between the selected Checker and the specified position(excluding).
     * If no Checker is selected, then the result is -1.
     * If the position of the selected Checker and the specified position do not line up(are not on the same diagonal),
     * then the result is -1.
     *
     * @param positionEnd the position until which to perform the counting
     * @return amount of Checkers between the selected Checker and the specified position(excluding),
     * -1 in case of invalid arguments
     */
    private int amountOfCheckersUntil(Vector2i positionEnd) {
        final Checker checker = getChecker(positionOfSelectedChecker);
        if (checker == null) { // if a checker is not selected
            return -1;
        }

        final Vector2i positionStart = positionOfSelectedChecker;

        Vector2i currentPosition = positionStart.clone();
        final Vector2i delta = new Vector2i(positionEnd.x - positionStart.x, positionEnd.y - positionStart.y);
        final int iteratorX = (delta.x > 0) ? +1 : -1;
        final int iteratorY = (delta.y > 0) ? +1 : -1;

        if (delta.x == 0 || delta.y == 0) {
            return -1;
        }

        delta.x = Math.abs(delta.x);
        delta.y = Math.abs(delta.y);

        if (delta.x != delta.y) {
            // Then the direction was not diagonal
            return -1;
        }

        // The move cannot consist of more steps than maxAmountOfMoves.
        // So if this happens to be the case then positionEnd was incorrect
        final int maxAmountOfMoves = delta.x; // == delta.y
        int count = 0; // to account for the Checker at positionStart
        for (int stepIndex = 0; stepIndex < maxAmountOfMoves - 1; stepIndex++) {
            currentPosition.x += iteratorX;
            currentPosition.y += iteratorY;

            if (getChecker(currentPosition) != null) {
                count++;
            }
        }

//        if (!currentPosition.equals(positionEnd)) {
//            // The path was incorrect
//            return -1;
//        }

        return count;
    }

    /**
     * Converts the given Vector2d into Vector2i by dividing by cellSize.
     * Expects the given vector to be a position on the board.
     *
     * @param position the position on the board stored in Vector2d.
     * @return position on the board stored in Vector2i.
     */
    public Vector2i convertPositionToVector2i(Vector2d position) {
        final double cellSize = CheckersSettings.getInstance().cellSize;
        return new Vector2i((int) (position.x / cellSize), (int) (position.y / cellSize));
    }

    private boolean doCheckersBelongToTheSamePlayer(Checker checker1, Checker checker2) {
        try {
            return checker1.getPlayerSide().equals(checker2.getPlayerSide());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public int getAmountOfCheckersOnBoard() {
        int amount = 0;
        for (Checker[] checkersRow : board) {
            for (Checker checker : checkersRow) {
                if (checker != null) {
                    amount++;
                }
            }
        }

        return amount;
    }
}
