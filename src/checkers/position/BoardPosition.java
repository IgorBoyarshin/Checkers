package checkers.position;

import checkers.CheckersSettings;
import checkers.util.Vector2d;
import checkers.util.Vector2i;

/**
 * This class describes a cell position that is restricted to the cells' grid,
 * so the possible positions are discreet.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class BoardPosition implements Positionable {

    /**
     * The position in board space(indices on the board).
     */
    private Vector2i boardPosition;

    /**
     * The actual position within the canvas(in pixels).
     */
    private Vector2d actualPosition;

    /**
     * The constructor for the class, initializes the positions with the given position on the board.
     *
     * @param boardPosition the position on the board.
     */
    public BoardPosition(Vector2i boardPosition) {
        this.boardPosition = boardPosition;

        recalculateActualPosition();
    }

    /**
     * Sets a new board position.
     *
     * @param newBoardPosition the new board position.
     */
    public void setPosition(Vector2i newBoardPosition) {
        this.boardPosition = newBoardPosition;
        recalculateActualPosition();
    }

    /**
     * Returns the current board position.
     *
     * @return the current board position.
     */
    public Vector2i getPosition() {
        return boardPosition;
    }

    /**
     * Recalculates the current actual position based on current board position and cell size(in pixels).
     */
    private void recalculateActualPosition() {
        final double cellSize = CheckersSettings.getInstance().cellSize;
        this.actualPosition = new Vector2d(boardPosition.x * cellSize, boardPosition.y * cellSize);
    }

    /**
     * Returns the current actual position.
     *
     * @return the current actual position.
     */
    @Override
    public Vector2d getActualPosition() {
        return actualPosition;
    }
}
