package checkers.checker;

import checkers.CheckersSettings;
import checkers.players.PlayerSide;
import checkers.position.BoardPosition;
import checkers.position.Positionable;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;
import checkers.util.Vector2i;

/**
 * Created by Igorek on 09-Apr-17 at 8:50 AM.
 */
public class Checker {
    private final CheckerColor color;
    private final PlayerSide playerSide;
//    private final double cellSize;

    private boolean queen;

    private boolean selected;

    private Positionable position;

    private final DrawingConstants dc;

    public Checker(CheckerColor color, PlayerSide playerSide, Vector2i boardPosition) {
        this.color = color;
        this.playerSide = playerSide;
        this.queen = false;

        this.position = new BoardPosition(boardPosition);
        this.dc = new DrawingConstants();
    }

    public void draw(GraphicsContext gc) {
        final Vector2d cellActualPosition = position.getActualPosition();
        final Vector2d upperLeftCheckerCorner = new Vector2d(
                cellActualPosition.x + dc.checkerShiftFromCell,
                cellActualPosition.y + dc.checkerShiftFromCell);

        // If the checker is selected => draw a circle around it. (Imitate with a circle of bigger size)
        if (selected) {
            gc.setFill(color.selectionColor);
            gc.fillOval(
                    upperLeftCheckerCorner.x + dc.selectionShiftFromChecker,
                    upperLeftCheckerCorner.y + dc.selectionShiftFromChecker,
                    dc.selectionSize, dc.selectionSize);
        }

        // Checker background(main volume)
        gc.setFill(color.primaryColor);
        gc.fillOval(upperLeftCheckerCorner.x, upperLeftCheckerCorner.y, dc.checkerSize, dc.checkerSize);

        // Checker decorations
        if (!queen) {
            gc.setStroke(color.secondaryColor);
            gc.strokeOval(
                    upperLeftCheckerCorner.x + dc.firstInnerCircleShiftFromChecker,
                    upperLeftCheckerCorner.y + dc.firstInnerCircleShiftFromChecker,
                    dc.firstInnerCircleSize, dc.firstInnerCircleSize);
            gc.strokeOval(
                    upperLeftCheckerCorner.x + dc.secondInnerCircleShiftFromChecker,
                    upperLeftCheckerCorner.y + dc.secondInnerCircleShiftFromChecker,
                    dc.secondInnerCircleSize, dc.secondInnerCircleSize);
        }
    }

    public void update(double secondsSinceStart) {
        position.update(secondsSinceStart);
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    /**
     * @return whether this checker is selected after performing the flip.
     */
    // TODO: consider useless
    public boolean flipSelection() {
        selected = !selected;

        return selected;
    }

    public boolean isQueen() {
        return queen;
    }

    public void makeQueen() {
        queen = true;
    }

    public PlayerSide getPlayerSide() {
        return playerSide;
    }

    public boolean isCheckerDirectionUp() {
        return playerSide.equals(PlayerSide.PLAYER_DOWN);
    }


    private class DrawingConstants {
        private DrawingConstants() {
            final double cellSize = CheckersSettings.getInstance().cellSize;

            // Checker itself
            final double checkerSizeMultiplier = 0.7; // checker's size relative to cell's size
            this.checkerSize = cellSize * checkerSizeMultiplier;
            this.checkerShiftFromCell = (cellSize - checkerSize) / 2.0;

            // Decorations
            final double firstInnerCircleSizeMultiplier = 0.8; // circle's size relative to checker's size
            this.firstInnerCircleSize = firstInnerCircleSizeMultiplier * checkerSize;
            this.firstInnerCircleShiftFromChecker = (checkerSize - firstInnerCircleSize) / 2.0;
            final double secondInnerCircleSizeMultiplier = 0.4; // circle's size relative to checker's size
            this.secondInnerCircleSize = secondInnerCircleSizeMultiplier * checkerSize;
            this.secondInnerCircleShiftFromChecker = (checkerSize - secondInnerCircleSize) / 2.0;

            // Selection
            final double selectionSizeMultiplier = 1.2; // selection's size relative to checker's size
            this.selectionSize = checkerSize * selectionSizeMultiplier;
            this.selectionShiftFromChecker = (checkerSize - selectionSize) / 2.0; // Will be negative
        }

        public final double checkerShiftFromCell;
        public final double checkerSize;

        public final double firstInnerCircleSize;
        public final double secondInnerCircleSize;
        public final double firstInnerCircleShiftFromChecker;
        public final double secondInnerCircleShiftFromChecker;

        public final double selectionSize;
        public final double selectionShiftFromChecker;
    }
}
