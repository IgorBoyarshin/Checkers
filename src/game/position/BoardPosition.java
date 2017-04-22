package game.position;

import util.Vector2d;
import util.Vector2i;

/**
 * This class describes a cell position that is restricted to the cells' grid,
 * so the possible positions are discreet.
 *
 * Created by Igorek on 09-Apr-17 at 9:00 AM.
 */
public class BoardPosition implements Positionable {

    private final double cellSize;
    private Vector2i boardPosition;
    private Vector2d actualPosition;

    public BoardPosition(Vector2i boardPosition, double cellSize) {
        this.boardPosition = boardPosition;
        this.cellSize = cellSize;

        recalculateActualPosition();
    }

    public void setPosition(Vector2i newBoardPosition) {
        this.boardPosition = newBoardPosition;
        recalculateActualPosition();
    }

    public Vector2i getPosition() {
        return boardPosition;
    }

    private void recalculateActualPosition() {
        this.actualPosition = new Vector2d(boardPosition.x * cellSize, boardPosition.y * cellSize);
    }

    @Override
    public Vector2d getActualPosition() {
        return actualPosition;
    }
}
