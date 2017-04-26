package checkers.checker;

import checkers.CheckersSettings;
import checkers.players.PlayerSide;
import checkers.position.BoardPosition;
import checkers.position.MovingPosition;
import checkers.position.Positionable;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;
import checkers.util.Vector2i;
import javafx.scene.paint.Color;

/**
 * This class describes a checker.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class Checker {
    /**
     * The color scheme of the Checker.
     */
    private final CheckerColor color;

    /**
     * The side that the Checker belongs to.
     */
    private final PlayerSide playerSide;

    /**
     * Whether this Checker is a queen.
     */
    private boolean queen;

    /**
     * Whether this Checker is currently selected.
     */
    private boolean selected;

    /**
     * The position of the Checker.
     */
    private Positionable position;

    /**
     * The constants used for drawing the Checker.
     */
    private final DrawingConstants dc;

    /**
     * Whether this Checker is currently dying.
     * A Checker starts dying when it has been eaten.
     */
    private boolean dying;

    /**
     * The time when the dying started(in seconds).
     */
    private double dyingTimeStart;

    /**
     * The progress of the dying process.
     * 0 - just started, 1 - has finished.
     */
    private double dyingProgress;

    /**
     * The constructor of this class.
     *
     * @param color         the color scheme of this Checker.
     * @param playerSide    the side that this Checker belongs to.
     * @param boardPosition the position on the Board of this Checker.
     */
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

    /**
     * Draws this Checker on the board.
     * Draws the background, the decorations and the selection if the Checker is currently selected.
     * Draws the dying animation of the Checker if it is dying.
     *
     * @param gc the thing to draw with.
     */
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

    /**
     * Updates the Checker.
     * Updates the progress of the dying or switches the type of the Position to MovingPosition if
     * the Checker has reached its moving destination.
     *
     * @param secondsSinceStart the time elapsed since the start of the game.
     */
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

    /**
     * Initiates the movement of the Checker into the new Position on the board.
     *
     * @param newPosition the Position where the Checker must move.
     */
    public void move(Vector2i newPosition) {
        this.position = new MovingPosition(
                ((BoardPosition) this.position),
                new BoardPosition(newPosition),
                CheckersSettings.getInstance().movementSpeed);
    }

    /**
     * Returns whether this Checker is currently selected.
     *
     * @return whether this Checker is currently selected.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Selects the Checker.
     */
    public void select() {
        selected = true;
    }

    /**
     * Deselects the Checker.
     */
    public void deselect() {
        selected = false;
    }

    /**
     * Flips the selection into the opposite.
     *
     * @return whether this checker is selected after performing the flip.
     */
    // TODO: consider useless
    public boolean flipSelection() {
        selected = !selected;

        return selected;
    }

    /**
     * Returns whether the Checker is currently dying.
     *
     * @return whether the Checker is currently dying.
     */
    public boolean isDying() {
        return dying;
    }

    /**
     * Makes the Checker start dying.
     */
    public void die() {
        dying = true;
    }

    /**
     * Returns whether the Checker has finished the process of dying.
     *
     * @return whether the Checker has finished the process of dying.
     */
    public boolean hasDied() {
        return dyingProgress >= 1.0;
    }

    /**
     * Returns whether the Checker is a queen.
     *
     * @return whether the Checker is a queen.
     */
    public boolean isQueen() {
        return queen;
    }

    /**
     * Makes the Checker a queen.
     */
    public void makeQueen() {
        queen = true;
    }

    /**
     * Returns the side that the Checker belongs to.
     *
     * @return the side that the Checker belongs to.
     */
    public PlayerSide getPlayerSide() {
        return playerSide;
    }

    /**
     * Checks whether the specified movement direction(movement delta along Y axis) is the same as
     * allowed movement direction(specified in PlayerSide.forwardDirectionY).
     *
     * @param deltaY the direction step to check.
     * @return whether this Checker is allowed to move in the specified direction along Y axis.
     */
    public boolean isMovementDirectionAllowed(double deltaY) {
        return Math.signum(deltaY) == Math.signum(playerSide.forwardDirectionY);
    }

    /**
     * Checks whether the Checker belongs to the specified PlayerSide.
     *
     * @param playerSide the side to check whether the Checker belongs to.
     * @return whether the Checker belongs to the specified PlayerSide.
     */
    public boolean belongsToPlayerSide(PlayerSide playerSide) {
        return this.playerSide.equals(playerSide);
    }

    /**
     * Checks whether the given Checkers belong to the same PlayerSide.
     *
     * @param checker1 the first Checker for comparison.
     * @param checker2 the second Checker for comparison.
     * @return whether the given Checkers belong to the same PlayerSide.
     */
    public static boolean doCheckersBelongToTheSamePlayer(Checker checker1, Checker checker2) {
        try {
            return checker1.playerSide.equals(checker2.playerSide);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Checks whether the movement direction of the Checker is UP.
     *
     * @return whether the movement direction of the Checker is UP.
     */
    public boolean isCheckerDirectionUp() {
        return playerSide.equals(PlayerSide.PLAYER_DOWN);
    }


    /**
     * This class describes the drawing constants that are used during the drawing of a Checker.
     */
    private class DrawingConstants {
        /**
         * The constructor of the class
         */
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

        /**
         * The shift in pixels of the Checker from the side of the cell.
         */
        private final double checkerShiftFromCell;

        /**
         * The side of the Checker in pixels.
         */
        private final double checkerSize;

        /**
         * The size(radius) in pixels of the first inner circle decoration of the Checker.
         */
        private final double firstInnerCircleSize;

        /**
         * The size(radius) in pixels of the second inner circle decoration of the Checker.
         */
        private final double secondInnerCircleSize;

        /**
         * The shift in pixels from the side of the cell of the first inner circle decoration/
         */
        private final double firstInnerCircleShiftFromChecker;

        /**
         * The shift in pixels from the side of the cell of the second inner circle decoration/
         */
        private final double secondInnerCircleShiftFromChecker;

        /**
         * The size in pixels of the selection circle.
         */
        private final double selectionSize;

        /**
         * The shift in pixels from the side of the cell of the selection.
         */
        private final double selectionShiftFromChecker;

        /**
         * The transparent color constant.
         */
        private final Color transparentColor;
    }
}
