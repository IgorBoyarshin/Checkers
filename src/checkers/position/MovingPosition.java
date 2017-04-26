package checkers.position;

import checkers.util.Vector2d;
import checkers.util.Vector2i;

/**
 * Describes a position that changes its coordinates overt time.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class MovingPosition implements Positionable {

    /**
     * The position of the cell(in pixels) where the movement started.
     */
    private final BoardPosition positionStart;

    /**
     * The position of the cell where the movement must end.
     */
    private final BoardPosition positionEnd;

    /**
     * The time(in seconds) when the movement started.
     * Gets initialized during the first call to the update() method.
     */
    private double movementTimeStartInSeconds;

    /**
     * The current actual position(in pixels).
     */
    private Vector2d currentPosition;

    /**
     * The movement speed of the position.
     */
    private final MovementSpeed movementSpeed;

    /**
     * Whether the movement has completed or not.
     */
    private boolean completed;

    /**
     * @param positionStart the position(in pixels) where the movement started.
     * @param positionEnd   the position(in pixels) where the movement must end.
     */
    public MovingPosition(BoardPosition positionStart, BoardPosition positionEnd,
                          MovementSpeed movementSpeed) {
        super();
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
        this.movementSpeed = movementSpeed;

        this.movementTimeStartInSeconds = 0.0;
        this.currentPosition = positionStart.getActualPosition().clone();
        this.completed = false;
    }

    /**
     * Returns the actual position in pixels.
     *
     * @return the actual position in pixels.
     */
    @Override
    public Vector2d getActualPosition() {
        return currentPosition;
    }

    /**
     * Updates the position based on time elapsed since the start.
     *
     * @param currentSecondsSinceStart seconds elapsed since the start of the game.
     */
    @Override
    public void update(double currentSecondsSinceStart) {
        if (movementTimeStartInSeconds == 0.0) {
            // Initialize if this is the first call
            this.movementTimeStartInSeconds = currentSecondsSinceStart;
        }

        final double movementProgress = (currentSecondsSinceStart - movementTimeStartInSeconds) / movementSpeed.durationInSeconds;
        if (movementProgress < 1.0) {
            final Vector2d start = positionStart.getActualPosition();
            final Vector2d end = positionEnd.getActualPosition();

            this.currentPosition.x = 1.0 * start.x + (end.x - start.x) * movementProgress;
            this.currentPosition.y = 1.0 * start.y + (end.y - start.y) * movementProgress;
        } else {
            this.completed = true;
        }
    }

    /**
     * Returns the position where the movement must end.
     *
     * @return the position where the movement must end.
     */
    public Vector2i getDestinationPosition() {
        return positionEnd.getPosition();
    }

    /**
     * Returns whether the movement has completed or not.
     *
     * @return whether the movement has completed or not.
     */
    public boolean hasCompleted() {
        return completed;
    }
}
