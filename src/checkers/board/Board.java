package checkers.board;

import checkers.checker.Checker;
import checkers.checker.CheckerColor;
import checkers.CheckersSettings;
import checkers.players.PlayerSide;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;
import checkers.util.Vector2i;

/**
 * This class describes a game board.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class Board {

    /**
     * Amount of cells on each side of the board.
     */
    private final int boardSizeInCells;

    /**
     * The color scheme of the light and dark cells of the Board.
     */
    private final BoardCellColor boardCellColor;

    /**
     * The array containing all the Checkers as a grid.
     * If a cell is empty(== null), then there is no Checker in it.
     * <p>
     * Access: board[column][row]
     */
    private final Checker[][] board;

    /**
     * The color scheme of the Checkers of the UP Player.
     */
    private final CheckerColor playerUpCheckerColor;

    /**
     * The color scheme of the Checkers of the DOWN Player.
     */
    private final CheckerColor playerDownCheckerColor;

    /**
     * The position on the board of the currently selected Checker.
     */
    private Vector2i positionOfSelectedChecker;

    /**
     * The Checker to be drawn on top.
     * That is the Checker that is currently moving, so it has to be drawn last(on top) in order to avoid
     * it sliding under some other Checker.
     */
    private Checker checkerToDrawOnTop;

    /**
     * The Checker that is currently dying.
     */
    // TODO: consider having only positionOfDyingChecker variable
    private Checker dyingChecker;

    /**
     * The position of the Checker that is currently dying.
     */
    private Vector2i positionOfDyingChecker;

    /**
     * The constructor of the class.
     *
     * @param boardSizeInCells       the size of the board in cells.
     * @param playerUpCheckerColor   the color scheme of the Checkers of the UP Player.
     * @param playerDownCheckerColor the color scheme of the Checkers of the DOWN Player.
     * @param boardCellColor         the color scheme of the cells of the Board.
     */
    public Board(
            int boardSizeInCells,
            CheckerColor playerUpCheckerColor, CheckerColor playerDownCheckerColor,
            BoardCellColor boardCellColor) {

        this.boardSizeInCells = boardSizeInCells;
        this.playerUpCheckerColor = playerUpCheckerColor;
        this.playerDownCheckerColor = playerDownCheckerColor;
        this.boardCellColor = boardCellColor;

        this.board = new Checker[boardSizeInCells][boardSizeInCells];
        initializeBoard();

        this.positionOfSelectedChecker = null;
        this.checkerToDrawOnTop = null;
        this.dyingChecker = null;
        this.positionOfDyingChecker = null;
    }

    /**
     * Initializes the board with Checkers.
     */
    private void initializeBoard() {
        if (false) {
            initializeBoardFromArray(new int[][]
                    {
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0, 0, 2, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 1, 0, 0, 0, 0, 0},
                            {0, 0, 0, 2, 0, 0, 0, 0},
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
                        checker = new Checker(playerUpCheckerColor, PlayerSide.PLAYER_UP, boardPosition);
                    } else {
                        checker = new Checker(playerDownCheckerColor, PlayerSide.PLAYER_DOWN, boardPosition);
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
     *
     * @param array the array to initialize the Board from.
     */
    private void initializeBoardFromArray(int[][] array) {
        for (int row = 0; row < boardSizeInCells; row++) {
            for (int column = 0; column < boardSizeInCells; column++) {
                final Vector2i boardPosition = new Vector2i(column, row);

                final Checker checker;
                switch (array[row][column]) {
                    case 1: // PLAYER_UP Checker
                        checker = new Checker(playerUpCheckerColor, PlayerSide.PLAYER_UP, boardPosition);
                        break;
                    case -1: // PLAYER_UP Checker queen
                        checker = new Checker(playerUpCheckerColor, PlayerSide.PLAYER_UP, boardPosition);
                        checker.makeQueen();
                        break;
                    case 2: // PLAYER_DOWN Checker
                        checker = new Checker(playerDownCheckerColor, PlayerSide.PLAYER_DOWN, boardPosition);
                        break;
                    case -2: // PLAYER_DOWN Checker queen
                        checker = new Checker(playerDownCheckerColor, PlayerSide.PLAYER_DOWN, boardPosition);
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

    /**
     * Returns whether a Checker must be places at the given position on the Board.
     *
     * @param row    a row index on the board.
     * @param column a column index on the board.
     * @return whether to place a Checker at the given position.
     */
    private boolean isCheckerHere(int row, int column) {
        return (row + column) % 2 == 1;
    }

    /**
     * Returns whether a dark cell is located at the given position on the Board.
     *
     * @param row    a row index on the board.
     * @param column a column index on the board.
     * @return whether a dark cell is located iat the given position.
     */
    private boolean isDarkCellHere(int row, int column) {
        return (row + column) % 2 == 1;
    }

    /**
     * Draws the board and the Checkers on it.
     *
     * @param gc the thing to draw with.
     */
    public void draw(GraphicsContext gc) {
        drawBoard(gc);
        drawCheckers(gc);
    }

    /**
     * Draws the board.
     *
     * @param gc the thing to draw with.
     */
    private void drawBoard(GraphicsContext gc) {
        final double cellSize = CheckersSettings.getInstance().cellSize;
        for (int row = 0; row < boardSizeInCells; row++) {
            for (int column = 0; column < boardSizeInCells; column++) {
                gc.setFill(isDarkCellHere(row, column) ? boardCellColor.darkColor : boardCellColor.lightColor);
                gc.fillRect(column * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }

    /**
     * Draws the Checkers.
     *
     * @param gc the thing to draw with.
     */
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

    /**
     * Updates the board and the Checkers.
     * Also handles the Checker that has dies.
     *
     * @param secondsSinceStart time in seconds elapsed since the start of the game.
     */
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
     * If the position is valid and the Checker at that position is present,
     * selects the Checker at the given position on the board, and removes any selection otherwise.
     * If the position is not valid, does nothing.
     * If the Checker does not belong to the specified PlayerSide, no selection is done.
     * Deselects the previously selected Checker.
     *
     * @param playerSide      the PlayerSide to which the Checker must belong to in order to be able to select it.
     * @param checkerPosition the position on the board where the selection should happen.
     * @return whether any checker is selected after performing this operation.
     */
    public boolean selectChecker(PlayerSide playerSide, Vector2i checkerPosition) {
        final Checker checker = getChecker(checkerPosition);
        if (checker == null) {
            return false;
        }
        if (!checker.belongsToPlayerSide(playerSide)) {
            return false;
        }

        final Checker previouslySelectedChecker = getChecker(positionOfSelectedChecker);

        if (checkerPosition.equals(positionOfSelectedChecker)) {
            // If we clicked on the selected checker
            checker.deselect();
            positionOfSelectedChecker = null;
        } else {
            if (previouslySelectedChecker != null) {
                previouslySelectedChecker.deselect();
                positionOfSelectedChecker = null;
            }

            checker.select();
            positionOfSelectedChecker = checkerPosition;
        }

        return isCheckerSelected();
    }

    /**
     * Deselects any Checker that was selected before, does nothing otherwise.
     *
     * @return true if a Checker was selected before.
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
     * Returns a copy of the position of the selected Checker.
     *
     * @return a copy of the position of the selected Checker.
     */
    public Vector2i getPositionOfSelectedChecker() {
        return positionOfSelectedChecker.clone();
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
     * Kill the Checker that this Checker eats(if any).
     *
     * @param newPosition the position into which the Checker must move.
     * @return true if this Checker eats any Checker as the result of its move, false otherwise.
     */
    public boolean moveSelectedChecker(Vector2i newPosition) {
        final Checker selectedChecker = getChecker(positionOfSelectedChecker);
        if (selectedChecker == null) {
            return false;
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

        // Make it a queen if it has reached the end of the board
        final float forwardDirection = selectedChecker.getPlayerSide().forwardDirectionY;
        if ((newPosition.y == 0 && forwardDirection < 0)
                || (newPosition.y == boardSizeInCells - 1 && forwardDirection > 0)) {
            selectedChecker.makeQueen();
        }


        // Set this Checker to be rendered on top.
        // At any time there will be a not-null reference.
        // This is OK because drawing order matters only during movement
        checkerToDrawOnTop = selectedChecker;

        // Deselect manually
        // TODO: do this with a method
        selectedChecker.deselect();
        positionOfSelectedChecker = null;

//        deselectChecker();

        return checkerToEat != null;
    }

    /**
     * Checks whether the selected checker is allowed to move into the specified cell.
     * Will ban the move if (eating is mandatory AND no eating during the move).
     *
     * @param newPosition the position where the Checker should move.
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
                    if (!selectedChecker.isMovementDirectionAllowed(delta.y)) {
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
            if (Checker.doCheckersBelongToTheSamePlayer(selectedChecker, potentialCheckerToEat)) {
                // If the Checkers belong to the same Player => can't eat => invalid move
                return false;
            }
        }

//        System.out.println("Can eat: " + canSelectedCheckerEat());
        if (CheckersSettings.getInstance().isEatingMandatory && !checkerEats && canPlayerEat(selectedChecker.getPlayerSide())) {
            // TODO: mb print into a Label that the move is banned due to not eating
            return false;
        }

        return true;
    }

    /**
     * Returns whether the Checker at the specified position can eat now.
     *
     * @param position the position of the Checker.
     * @return whether the Checker at the specified position can eat now.
     */
    public boolean canCheckerEat(Vector2i position) {
        final Checker checker = getChecker(position);
        if (checker == null) { // if a checker is not selected
            return false;
        }

        // Strategy:
        // Go in all 4 diagonal directions(allowed movement length is determined by being a queen).
        // If a Checker of the same PlayerSide is found => can't eat in that direction.
        // If a Checker of the opposite PlayerSide is found =>
        // => can eat if the distance to it is correct(queen or not) and if the cell after this Checker is empty

        // How much cells we can move before we must encounter a Checker to eat
        // (before we are in the cell where that Checker is).
        final int allowedMoveLengthUntilEating;
        if (checker.isQueen()) {
            allowedMoveLengthUntilEating = boardSizeInCells - 1; // minus our cell
        } else { // not a queen
            allowedMoveLengthUntilEating = 1;
        }

        // Cycle for all 4 diagonal directions
        for (int deltaX = -1; deltaX <= +1; deltaX += 2) {
            for (int deltaY = -1; deltaY <= +1; deltaY += 2) {
                final Vector2i currentPosition = position.clone();
                Checker checkerToEat = null;

                // Moving in that direction
                for (int currentMoveLength = 0; currentMoveLength < allowedMoveLengthUntilEating; currentMoveLength++) {
                    currentPosition.x += deltaX;
                    currentPosition.y += deltaY;

                    if (!isPositionValid(currentPosition)) {
                        // If we went outside the board => found nobody
                        break;
                    }

                    checkerToEat = getChecker(currentPosition);
                    if (checkerToEat != null) {
                        // Found someone => stop moving
                        break;
                    }
                }

                // Finished moving
                if (checkerToEat != null) {
                    // Found someone
                    if (Checker.doCheckersBelongToTheSamePlayer(checkerToEat, checker)) {
                        // Can't eat because the Checkers belong to the same Player
                        continue;
                    } else {
                        // Make one more step
                        currentPosition.x += deltaX;
                        currentPosition.y += deltaY;

                        if (isPositionValid(currentPosition) && getChecker(currentPosition) == null) {
                            // The cell after the found Checker is within the board and is empty => can more there
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns whether the given PlayerSide can eat any Checker now.
     *
     * @param playerSide the playerSide to perform the check for.
     * @return whether the given PlayerSide can eat any Checker now.
     */
    public boolean canPlayerEat(PlayerSide playerSide) {
        for (int column = 0; column < boardSizeInCells; column++) {
            for (int row = 0; row < boardSizeInCells; row++) {
                final Vector2i position = new Vector2i(column, row);
                final Checker checker = getChecker(position);

                if (checker != null) {
                    if (checker.belongsToPlayerSide(playerSide)) {
                        if (canCheckerEat(position)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns whether the given PlayerSide can make any move now.
     *
     * @param playerSide the PlayerSide for which to perform the Check.
     * @return whether the given PlayerSide can make any move now.
     */
    // TODO:
    public boolean canPlayerMakeAnyMove(PlayerSide playerSide) {
        return true;
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

        return count;
    }

    /**
     * Checks if the specified position is within the board bounds.
     *
     * @return true is the specified position os a valid board address, false otherwise.
     */
    private boolean isPositionValid(Vector2i position) {
        return (position.x >= 0 && position.x < boardSizeInCells) && (position.y >= 0 && position.y < boardSizeInCells);
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

    /**
     * Calculates the amount of Checker of the given PlayerSide currently present on the Board.
     *
     * @param playerSide the PlayerSide for which to perform the Check.
     * @return the amount of Checkers of the given PlayerSide currently present on the Board.
     */
    public int getAmountOfCheckersOnBoard(PlayerSide playerSide) {
        int amount = 0;
        for (Checker[] checkersRow : board) {
            for (Checker checker : checkersRow) {
                if (checker != null) {
                    if (checker.belongsToPlayerSide(playerSide)) {
                        amount++;
                    }
                }
            }
        }

        return amount;
    }
}
