package checkers.checker;

import checkers.CheckersSettings;
import checkers.players.PlayerSide;
import checkers.position.BoardPosition;
import checkers.position.MovementSpeed;
import checkers.position.MovingPosition;
import checkers.position.Positionable;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;
import checkers.util.Vector2i;
import javafx.scene.paint.Color;

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

    private boolean dying;

    /**
     * The time when the dying started(in seconds).
     */
    private double dyingTimeStart;

    private double dyingProgress;

    public Checker(CheckerColor color, PlayerSide playerSide, Vector2i boardPosition) {
        this.color = color;
        this.playerSide = playerSide;
        this.queen = false;

        this.position = new BoardPosition(boardPosition);
        this.dc = new DrawingConstants();
        this.dying = false;
        this.dyingTimeStart = 0.0;
        this.dyingProgress = 0.0;
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
        final Color fillColor = color.primaryColor.interpolate(dc.transparentColor, dyingProgress);
        gc.setFill(fillColor);
        gc.fillOval(upperLeftCheckerCorner.x, upperLeftCheckerCorner.y, dc.checkerSize, dc.checkerSize);

        // Checker decorations
        if (!queen) {
            final Color strokeColor = color.secondaryColor.interpolate(dc.transparentColor, dyingProgress);
            gc.setStroke(strokeColor);
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

        if (dying) {
            if (dyingTimeStart == 0.0) {
                // If we must start dying, but the dyingTimeStart has not been initialized yet => initialize it
                dyingTimeStart = secondsSinceStart;
            }

            final double dyingDuration = CheckersSettings.getInstance().movementSpeed.durationInSeconds;
            dyingProgress = (secondsSinceStart - dyingTimeStart) / dyingDuration;

            if (dyingProgress >= 1.0) {
                // We will be deleted in the board class
                dyingProgress = 1.0;
            }
        }

        if (this.position instanceof MovingPosition) {
            final MovingPosition movingPosition = (MovingPosition) this.position;
            if (movingPosition.hasCompleted()) {
                this.position = new BoardPosition(movingPosition.getDestinationPosition());
            }
        }
    }

    public void move(Vector2i newPosition) {
        this.position = new MovingPosition(
                ((BoardPosition) this.position),
                new BoardPosition(newPosition),
                CheckersSettings.getInstance().movementSpeed);
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

    public boolean isDying() {
        return dying;
    }

    public void die() {
        dying = true;
    }

    public boolean hasDied() {
        return dyingProgress >= 1.0;
    }

    public boolean isQueen() {
        return queen;
    }

    public void makeQueen() {
        queen = true;
    }

//    public PlayerSide getPlayerSide() {
//        return playerSide;
//    }

    /**
     * Checks whether the specified movement direction(movement delta along Y axis) is the same as
     * allowed movement direction(specified in PlayerSide.forwardDirectionY).
     *
     * @return whether this Checker can move in the specified direction along Y axis.
     */
    public boolean isMovementDirectionAllowed(double deltaY) {
        return Math.signum(deltaY) == Math.signum(playerSide.forwardDirectionY);
    }

    public static boolean doCheckersBelongToTheSamePlayer(Checker checker1, Checker checker2) {
        try {
            return checker1.playerSide.equals(checker2.playerSide);
        } catch (NullPointerException e) {
            return false;
        }
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

            // Dying
            this.transparentColor = Color.rgb(255, 255, 255, 0.0);
        }

        private final double checkerShiftFromCell;
        private final double checkerSize;

        private final double firstInnerCircleSize;
        private final double secondInnerCircleSize;
        private final double firstInnerCircleShiftFromChecker;
        private final double secondInnerCircleShiftFromChecker;

        private final double selectionSize;
        private final double selectionShiftFromChecker;

        private final Color transparentColor;
    }
}
